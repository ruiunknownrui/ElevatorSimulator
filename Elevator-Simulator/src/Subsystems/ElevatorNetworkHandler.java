package Subsystems;

import java.io.IOException;
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


    ElevatorNetworkHandler(Elevator elevator, int port) throws SocketException {
        this.elevator = elevator;
        this.port = port;
        this.socket = new DatagramSocket(port);
    }
    public void run(){
        while (true){
            // prepare to receive request
            byte[] receivedData = new byte[100];
            DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);

            try {
                socket.receive(receivePacket);
            } catch (IOException e){
                e.printStackTrace();
                System.exit(1);
            }
            elevator.receiveRequest(receivePacket);
        }
    }
}
