package Subsystems;

import Data.Ack;
import Data.Event;
import States.SchedulerState;

import java.io.*;
import java.net.*;

/**
 * This class is created by the Scheduler and runs on its own thread, its job includes sending the request to
 * specific elevator, and receive acknowledge back from that specific elevator.
 * @author Rebecca Li
 */
public class SchedulerSendHandler implements Runnable{

    private RequestBuffer buffer;
    private Scheduler scheduler;

    private DatagramPacket sendPacket;  // Datagram packet for sending request to elevator

    private DatagramPacket receivePacket;  // Datagram packet for receiving acknowledge from floor
    private DatagramSocket socket;  // DatagramSocket which is used to receive and send
    private final int port = 3001;

    public SchedulerSendHandler(RequestBuffer buffer, Scheduler scheduler){
        this.buffer = buffer;
        this.scheduler = scheduler;
        try {
            this.socket = new DatagramSocket(this.port);  // Create Socket
            this.socket.setSoTimeout(3000); // Set time out to 3000 milliseconds
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Sends a message to the specific elevator and receive acknowledge
     */
    public void sendAndReceive() throws IOException {
        Event sendEvent = this.buffer.getNextEvent();  // Get event from the buffer
        int target = this.scheduler.targetElevator(sendEvent);  // Get the port of target elevator

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOut);
        output.writeObject(sendEvent);
        byte[] sendMsg = byteOut.toByteArray();

        try {
            this.sendPacket = new DatagramPacket(sendMsg, sendMsg.length, InetAddress.getLocalHost(), target);
            // Initialize receivePacket before using it
            byte receiveData[] = new byte[100];
            this.receivePacket = new DatagramPacket(receiveData, receiveData.length);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Perform sending and receiving with timeout handling
        int attempt = 0;
        boolean receivedResponse = false;

        while (attempt < 3 && !receivedResponse) { // Retry up to 3 times
            System.out.println(Thread.currentThread().getName() + ": Attempt " + (attempt + 1));
            rpc_send(sendPacket);

            try {
                this.socket.receive(receivePacket);  // Attempt to receive the acknowledgment
                this.handleAcknowledgment(receivePacket);  // Handle the acknowledgment
                receivedResponse = true;
                this.scheduler.setState(SchedulerState.updateState(true, false));
            } catch (SocketTimeoutException ste) {
                // Handle timeout exception
                System.out.println(Thread.currentThread().getName() + ": Timeout. Resending packet.");
                attempt++;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if (!receivedResponse) {
            System.out.println(Thread.currentThread().getName() + ": No response after multiple attempts. Exiting.");
            return;
        }

    }

    /**
     * Handles an acknowledgment packet received from the server.
     *
     * @param receivePacket The DatagramPacket containing the acknowledgment received from the server.
     */
    private void handleAcknowledgment(DatagramPacket receivePacket) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputByte = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream inputObject = new ObjectInputStream(inputByte);
        Ack receivedEvent = (Ack)inputObject.readObject();
        System.out.println("Scheduler receive: " + receivedEvent.getAck());
    }


    /**
     * Sends a request packet to the server and receives the response packet.
     *
     * @param request  the DatagramPacket representing the request.
     */
    public void rpc_send(DatagramPacket request) {
        try {
            this.socket.send(request);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(Thread.currentThread().getName() + ": Packet sent.\n");
    }
    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        while (true){
            try {
                this.sendAndReceive();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
