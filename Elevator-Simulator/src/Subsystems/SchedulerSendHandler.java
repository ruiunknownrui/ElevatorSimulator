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

    /**
     * initialize the buffer, the scheduler, and the socket
     * @param buffer  the request buffer
     * @param scheduler  the scheduler
     */
    public SchedulerSendHandler(RequestBuffer buffer, Scheduler scheduler){
        this.buffer = buffer;
        this.scheduler = scheduler;
        try {
            this.socket = new DatagramSocket(this.port);  // Create Socket
            this.socket.setSoTimeout(2000); // Set time out to 2 seconds
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Sends request event to the specific elevator and receive acknowledge
     */
    public void sendAndReceive() throws IOException {
//        System.out.println("send to elevator---");
        Event sendEvent = this.buffer.getNextEvent();  // Get event from the buffer
//        System.out.println("event: " + sendEvent.toString());
        int target = this.scheduler.targetElevator(sendEvent) + 1;  // Get the port of target elevator

//        System.out.println("Target port: " + target);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOut);
        output.writeObject(sendEvent);
        byte[] sendMsg = byteOut.toByteArray();

        try {
            this.sendPacket = new DatagramPacket(sendMsg, sendMsg.length, InetAddress.getLocalHost(), target);
            // Initialize receivePacket before using it
            byte receiveData[] = new byte[1000];
            this.receivePacket = new DatagramPacket(receiveData, receiveData.length);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Perform sending and receiving with timeout handling
        int attempt = 1;
        boolean receivedResponse = false;

        while (!receivedResponse) { // Keep sending until receive the response
            System.out.println(Thread.currentThread().getName() + ": Attempt " + attempt + " - Sending " +
                    sendEvent.toString());
            rpc_send(sendPacket);

            try {
                this.socket.receive(receivePacket);  // Attempt to receive the acknowledgment
                this.handleAcknowledgment(receivePacket);  // Handle the acknowledgment
                receivedResponse = true;
                this.scheduler.setState(SchedulerState.updateState(true, false));
            } catch (SocketTimeoutException ste) {
                // Handle timeout exception
//                System.out.println(Thread.currentThread().getName() + ": Timeout. Resending packet.");
                attempt++;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
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
