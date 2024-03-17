package Subsystems;

import Data.Ack;
import Data.Event;
import States.SchedulerState;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * This class is created by the Scheduler and runs on its own thread, its job includes receiving the floor request,
 * send acknowledge back to the floor, and add the received request to the buffer.
 * @author Rebecca Li
 */
public class SchedulerReceiveHandler implements Runnable{
    private RequestBuffer buffer;
    private Scheduler scheduler;

    private DatagramPacket ackPacket;  // Datagram packet for sending acknowledge to floor

    private DatagramPacket receivePacket;  // Datagram packet for receiving event from floor
    private DatagramSocket socket;  // DatagramSocket which is used to receive and send
    private final int port = 3000;

    public SchedulerReceiveHandler(RequestBuffer buffer, Scheduler scheduler){
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

    public void receiveRequest() throws IOException, ClassNotFoundException {
        byte[] receive = new byte[100];
        receivePacket = new DatagramPacket(receive, receive.length);

        try {
            socket.receive(receivePacket);  // Attempt to receive the acknowledgment
            sendAcknowledgment(receivePacket);  // Send back acknowledgement
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.addEventToBuffer(receivePacket);

    }

    public void addEventToBuffer(DatagramPacket receivePacket) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputByte = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream inputObject = new ObjectInputStream(inputByte);
        Event receivedEvent = (Event)inputObject.readObject();
        System.out.println("Scheduler receive " + receivedEvent.toString());
        this.buffer.addToEvents(receivedEvent);
        this.scheduler.setState(SchedulerState.updateState(true, false));
    }

    public void sendAcknowledgment(DatagramPacket receivedPacket) throws IOException {
        Ack newAck = new Ack("Receive request from floor");
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOut);
        output.writeObject(newAck);
        byte[] ackByte = byteOut.toByteArray();

        ackPacket = new DatagramPacket(ackByte, ackByte.length, receivedPacket.getAddress(), receivedPacket.getPort());
        try{
            this.socket.send(ackPacket);
            System.out.println("Send acknowledgment to floor!");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        while(true){
            try {
                receiveRequest();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
