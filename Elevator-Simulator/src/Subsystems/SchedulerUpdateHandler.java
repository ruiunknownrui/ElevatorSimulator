package Subsystems;

import Data.Ack;
import Data.ElevatorInfo;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * This class is created by the Scheduler and runs on its own thread, its job includes receiving
 * the elevator current position, send back acknowledge and update elevator information in Scheduler.
 * @author Rebecca Li
 */
public class SchedulerUpdateHandler implements Runnable{

    private Scheduler scheduler;

    private DatagramPacket ackPacket;  // Datagram packet for sending acknowledge to floor

    private DatagramPacket receivePacket;  // Datagram packet for receiving event from floor
    private DatagramSocket socket;  // DatagramSocket which is used to receive and send
    private final int port = 3002;

    /**
     * initialize the scheduler and socket
     * @param scheduler
     */
    public SchedulerUpdateHandler(Scheduler scheduler){
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
     * receiveInfo received the elevator information from all elevators
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void receiveInfo() throws IOException, ClassNotFoundException {
        byte[] receive = new byte[1000];
        receivePacket = new DatagramPacket(receive, receive.length);

        try {
            socket.receive(receivePacket);  // Attempt to receive the acknowledgment
            ElevatorInfo receivedInfo = this.getReceived(receivePacket);
            System.out.println("Scheduler receive: port-" + receivePacket.getPort() + ", elevator info-" + receivedInfo.toString());
            this.scheduler.updateElevatorInfo(receivePacket.getPort(), receivedInfo);
            this.sendAcknowledgment(receivePacket, receivedInfo);  // Send back acknowledgement
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * getReceived returns the received information in ElevatorInfo format
     * @param receivePacket  the received DatagramPacket
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ElevatorInfo getReceived(DatagramPacket receivePacket) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputByte = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream inputObject = new ObjectInputStream(inputByte);
        ElevatorInfo receivedInfo = (ElevatorInfo)inputObject.readObject();
        return receivedInfo;
    }

    /**
     * sendAcknowledgment creates and sends the acknowledgment to the specific elevator
     * @param receivedPacket  the received DatagramPacket
     * @throws IOException
     */
    public void sendAcknowledgment(DatagramPacket receivedPacket, ElevatorInfo elevatorInfo) throws IOException {
        Ack newAck = new Ack("Receive updated info from Elevator: " + elevatorInfo.toString());
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOut);
        output.writeObject(newAck);
        byte[] ackByte = byteOut.toByteArray();

        ackPacket = new DatagramPacket(ackByte, ackByte.length, receivedPacket.getAddress(), receivedPacket.getPort());
        try {
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
        while (true){
            try {
                this.receiveInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
