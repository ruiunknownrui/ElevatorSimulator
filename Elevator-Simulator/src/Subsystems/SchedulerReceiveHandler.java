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

    /**
     * Initialize the buffer, the scheduler, and the socket which is used to send and receive message
     * @param buffer  the request buffer
     * @param scheduler  the scheduler
     */
    public SchedulerReceiveHandler(RequestBuffer buffer, Scheduler scheduler){
        this.buffer = buffer;
        this.scheduler = scheduler;
        try {
            this.socket = new DatagramSocket(this.port);  // Create Socket
//            this.socket.setSoTimeout(2000); // Set time out to 2 second
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * receiveRequest receives request from floor
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void receiveRequest() throws IOException, ClassNotFoundException {
        byte[] receive = new byte[1000];
        receivePacket = new DatagramPacket(receive, receive.length);

        try {
            socket.receive(receivePacket);  // Attempt to receive the acknowledgment
            Event received = this.getReceivedEvent(receivePacket);
            System.out.println("Scheduler receive " + received.toString());
            sendAcknowledgment(receivePacket, received);  // Send back acknowledgement
            this.buffer.addToEvents(received);  // Add received event to buffer
            this.scheduler.setState(SchedulerState.updateState(true, false));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * getReceiveEvent return the receive information in Event format
     * @param receivePacket  the received DatagramPacket
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Event getReceivedEvent(DatagramPacket receivePacket) throws IOException, ClassNotFoundException {
      ByteArrayInputStream inputByte = new ByteArrayInputStream(receivePacket.getData());
      ObjectInputStream inputObject = new ObjectInputStream(inputByte);
      Event receivedEvent = (Event) inputObject.readObject();
      return receivedEvent;
    }

    /**
     * sendAcknowledgement creates and sends teh acknowledgement to the floor
     * @param receivedPacket  the received DatagramPacket
     * @throws IOException
     */
    public void sendAcknowledgment(DatagramPacket receivedPacket, Event received) throws IOException {
        Ack newAck = new Ack("Receive request from floor - " + received.toString());
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOut);
        output.writeObject(newAck);
        byte[] ackByte = byteOut.toByteArray();
        ackPacket = new DatagramPacket(ackByte, ackByte.length, receivedPacket.getAddress(), receivedPacket.getPort());
        try{
            this.socket.send(ackPacket);
            System.out.println("Send acknowledgment to floor! - " + received.toString());
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
                this.receiveRequest();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
