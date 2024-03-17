package Subsystems;

import Data.Ack;
import Data.Direction;
import Data.Event;
import States.SchedulerState;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class name: Floor
 * Purpose: Represents the Floor subsystem, sends events/requests to the Scheduler subsystem.
 */
public class Floor implements Runnable{

//    private Scheduler schedulerSystem;
    private DatagramPacket sendPacket;  // Datagram packet for sending request to scheduler

    private DatagramPacket receivePacket;  // Datagram packet for receiving acknowledge from scheduler
    private DatagramSocket socket;  // DatagramSocket which is used to receive and send
    private final int port = 2000;
    public Floor(){
        try {
            this.socket = new DatagramSocket(this.port);  // Create Socket
//            this.socket.setSoTimeout(3000); // Set time out to 3000 milliseconds
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Name: readInput()
     * Purpose: Opens the input file and reads it line by line, creating an Event object for each line of the file
     *          then returns the final list of all the Event objects.
     * In: Input file.
     * @return List of events read from input file.
     */
    public ArrayList<Event> readInput() {
        File events = new File("Elevator-Simulator/src/input.txt");
        ArrayList<Event> eventsData = new ArrayList<>();

        try {
            Scanner s = new Scanner(events);
            while (s.hasNextLine()) {
                String event = s.nextLine();
                System.out.println("Read the following event from the file: " + event);

                String[] eventData = event.split(" ");
                Direction d;
                if(eventData[2].equalsIgnoreCase("up")){
                    d = Direction.Up;
                } else if (eventData[2].equalsIgnoreCase("down")){
                    d = Direction.Down;
                } else {
                    return null;
                }

                Event e = new Event(eventData[0], Integer.parseInt(eventData[1]), d, Integer.parseInt(eventData[3]));
                eventsData.add(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return eventsData;
    }

    /**
     * Sends a message to the scheduler and receive acknowledge
     */
    public void sendAndReceive(Event sendEvent) throws IOException{

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOut);
        output.writeObject(sendEvent);
        byte[] sendMsg = byteOut.toByteArray();
        System.out.println("send byte: " + sendMsg);

        try {
            this.sendPacket = new DatagramPacket(sendMsg, sendMsg.length, InetAddress.getLocalHost(), 3000);
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
            rpc_send(sendPacket);

            try {
                this.socket.receive(receivePacket);  // Attempt to receive the acknowledgment
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
     * Handles an acknowledgment packet received from the server.
     *
     * @param receivePacket The DatagramPacket containing the acknowledgment received from the server.
     */
    private void handleAcknowledgment(DatagramPacket receivePacket) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputByte = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream inputObject = new ObjectInputStream(inputByte);
        Ack receivedEvent = (Ack)inputObject.readObject();
        System.out.println("Floor receive: " + receivedEvent.getAck());
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
     * Name: run()
     * Purpose: Run function for Floor thread. Loops through list of events read from input file and passes
     *          each one to the scheduler thread.
     * In: None
     * Out: None
     */
    public void run(){
        ArrayList<Event> eventsInput = readInput();
        for (Event event : eventsInput) {
            try {
                this.sendAndReceive(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try{
                Thread.sleep(300);
            } catch (InterruptedException e) { return; }
        }
    }

    public static void main(String[] args) {
        Thread floor;

        floor = new Thread( new Floor());

        floor.start();
    }
}
