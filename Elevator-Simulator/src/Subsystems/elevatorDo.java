package Subsystems;

import Data.Ack;
import Data.ElevatorInfo;

import java.io.*;
import java.net.*;

public class elevatorDo implements Runnable{

    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private Elevator elevator;
    private int port;

    public elevatorDo(Elevator elevator, int port){
        this.elevator = elevator;
        this.port = port;
        try {
            sendAndReceive();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

//    public void
////        moveElevator(this.elevator.getNextFloor());
////        moveElevator(this.elevator.getTargetFloor());
////        this.elevator.setHasRequest(false);
////    }doRequest() throws IOException {
//

    public void moveElevator(int targetFloor) throws IOException {
        while(targetFloor > this.elevator.getCurrentFloor()){
            this.elevator.increaseCurrentFloor();
            sendAndReceive();
        }
        while(targetFloor < this.elevator.getCurrentFloor()){
            this.elevator.decreaseCurrentFloor();
            sendAndReceive();
        }
    }

    /**
     * Sends a message to the scheduler and receive acknowledge
     */
    public void sendAndReceive() throws IOException {
        DatagramSocket socket = new DatagramSocket(port);

        ElevatorInfo elevatorInfo = new ElevatorInfo(this.elevator.getCurrentFloor());
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOut);
        output.writeObject(elevatorInfo);
        byte[] sendMsg = byteOut.toByteArray();

        try {
            this.sendPacket = new DatagramPacket(sendMsg, sendMsg.length, InetAddress.getLocalHost(), 3002);
            // Initialize receivePacket before using it
            byte receiveData[] = new byte[1000];
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
            rpc_send(sendPacket, socket);

            try {
                socket.receive(receivePacket);  // Attempt to receive the acknowledgment
                this.handleAcknowledgment(receivePacket);  // Handle the acknowledgment
                receivedResponse = true;
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
     * Handles an acknowledgment packet received from the scheduler.
     *
     * @param receivePacket The DatagramPacket containing the acknowledgment received from the server.
     */
    private void handleAcknowledgment(DatagramPacket receivePacket) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputByte = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream inputObject = new ObjectInputStream(inputByte);
        Ack receivedEvent = (Ack)inputObject.readObject();
        System.out.println("Elevator receive: " + receivedEvent.getAck());
    }


    /**
     * Sends a request packet to the scheduler.
     *
     * @param request  the DatagramPacket representing the request.
     */
    public void rpc_send(DatagramPacket request, DatagramSocket socket) {
        try {
            socket.send(request);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(Thread.currentThread().getName() + ": Packet sent.\n");
    }

    @Override
    public void run() {
//        while (true){
//            if (this.elevator.getHasRequest()){
//                try {
//                    doRequest();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            else {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException ignored) {
//                }
//            }
//        }
    }
}
