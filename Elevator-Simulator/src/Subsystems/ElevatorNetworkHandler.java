package Subsystems;

import Data.Ack;
import Data.Event;
import States.SchedulerState;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * This class is created by an elevator and runs on its own thread, its only job is to listen for network requests
 * and forward those requests to the elevator using the elevator's receiveRequest function.
 */
public class ElevatorNetworkHandler implements Runnable{
    private int port;
    private Elevator elevator;
    private DatagramSocket socket;
    private DatagramPacket receivePacket;
    private DatagramPacket ackPacket;


    ElevatorNetworkHandler(Elevator elevator, int port) throws SocketException {
        this.elevator = elevator;
        this.port = port;
        this.socket = new DatagramSocket(port);
        System.out.println("port in network: " + port);
    }

    /**
     * receiveRequest receives request from scheduler
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void receiveRequest() throws IOException, ClassNotFoundException {
        byte[] receive = new byte[1000];
        receivePacket = new DatagramPacket(receive, receive.length);

        try {
            socket.receive(receivePacket);  // Attempt to receive the acknowledgment
            sendAcknowledgment(receivePacket);  // Send back acknowledgement
            this.addEvent(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * addEventToBuffer sets the received event to the elevator
     * @param receivePacket  the received DatagramPacket
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void addEvent(DatagramPacket receivePacket) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputByte = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream inputObject = new ObjectInputStream(inputByte);
        Event receivedEvent = (Event) inputObject.readObject();
        System.out.println("Elevator " + port + " receive: " + receivedEvent.toString());
        this.elevator.updateRequest(receivedEvent);
    }

    /**
     * sendAcknowledgement creates and sends teh acknowledgement to the scheduler
     * @param receivedPacket  the received DatagramPacket
     * @throws IOException
     */
    public void sendAcknowledgment(DatagramPacket receivedPacket) throws IOException {
        Ack newAck = new Ack("Receive request from scheduler");
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOut);
        output.writeObject(newAck);
        byte[] ackByte = byteOut.toByteArray();
        ackPacket = new DatagramPacket(ackByte, ackByte.length, receivedPacket.getAddress(), receivedPacket.getPort());
        try{
            this.socket.send(ackPacket);
            System.out.println("Send acknowledgment to scheduler!");
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
//    public void run(){
//        while (true){
//            // prepare to receive request
//            byte[] receivedData = new byte[1000];
//            DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
//
//            try {
//                socket.receive(receivePacket);
//            } catch (IOException e){
//                e.printStackTrace();
//                System.exit(1);
//            }
//            elevator.receiveRequest(receivePacket);
//        }
//    }
}
