package Subsystems;

import Data.Ack;
import Data.ElevatorInfo;
import Data.Event;
import States.ElevatorButton;
import States.Door;
import States.Motor;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Elevator class simulates the behavior of an elevator car subsystem in the Elevator-Simulator project
 */
public class Elevator implements Runnable{


    private Thread networkHandler;
    private DatagramSocket updateSocket;  // The socket only used to update the elevator arrival information
    private int currentFloor;
    private int targetFloor;  // destination of the request
    private int nextFloor;  // start floor of the request
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private boolean hasRequest = false;
    private ElevatorButton[] elevatorButtons;
    private Door elevatorDoors;
    private Motor elevatorMotor;
    private Timer updateTimer;
    private static final double UPDATE_RATE = (double) 1/30; // Amount of seconds in between each update

    /**
     * Create an elevator that receive requests from the Scheduler
     */
    public Elevator(int port){
        //TODO: Change constructor to use port for network handler instead of directly using scheduler
        this.currentFloor = 1;;
        elevatorDoors = new Door();
        elevatorMotor = new Motor();

        elevatorButtons = new ElevatorButton[8];
        for(int i = 0; i < 8; i++){
            ElevatorButton newButton = new ElevatorButton(i+1);
            elevatorButtons[i] = newButton;
        }

        try {
            this.updateSocket = new DatagramSocket(port);
            this.updateSocket.setSoTimeout(2000);  // Set time out to 2 seconds
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }

        // setup network handler --- use for receive request from the Scheduler
        try {
            networkHandler = new Thread(new ElevatorNetworkHandler(this, port + 1));
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        startUpdateTimer();

        try {
            sendAndReceive();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        networkHandler.start();

    }

    //---------------------------------------------------------------------------------------
    public synchronized void updateRequest(Event event){
        while(hasRequest){
            try{
                wait();
            } catch (InterruptedException e){
                System.err.println(e);
            }
        }
        nextFloor = event.getFloor();
        targetFloor = event.getCarButton();
        hasRequest = true;
        notifyAll();
    }

    public synchronized void doRequest() throws IOException {
        while(!hasRequest){
            try{
                wait();
            } catch (InterruptedException e){
                System.err.println(e);
            }
        }

        moveElevator(this.nextFloor);
        moveElevator(this.targetFloor);
        hasRequest = false;
        notifyAll();
    }

    public boolean getHasRequest(){
        return hasRequest;
    }

    public void setHasRequest(boolean has){
        hasRequest = has;
    }

    public int getNextFloor(){
        return nextFloor;
    }

    public int getTargetFloor(){
        return targetFloor;
    }

    public void increaseCurrentFloor(){
        this.currentFloor += 1;
    }

    public void decreaseCurrentFloor(){
        this.currentFloor -= 1;
    }


    public void moveElevator(int targetFloor) throws IOException {
        while(targetFloor > currentFloor){
            currentFloor += 1;
            sendAndReceive();
        }
        while(targetFloor < currentFloor){
            currentFloor -= 1;
            sendAndReceive();
        }
    }

    /**
     * Sends a message to the scheduler and receive acknowledge
     */
    public void sendAndReceive() throws IOException {

        ElevatorInfo elevatorInfo = new ElevatorInfo(this.currentFloor, this.hasRequest);
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
        int attempt = 1;
        boolean receivedResponse = false;

        while (!receivedResponse) { // Keep sending until receive the acknowledgment
            System.out.println(Thread.currentThread().getName() + ": Attempt " + (attempt) +
                    " - Sending - " + elevatorInfo.toString());
            rpc_send(sendPacket);

            try {
                this.updateSocket.receive(receivePacket);  // Attempt to receive the acknowledgment
                this.handleAcknowledgment(receivePacket);  // Handle the acknowledgment
                receivedResponse = true;
            } catch (SocketTimeoutException ste) {
                // Handle timeout exception
//                System.out.println(Thread.currentThread().getName() + ": Timeout. Resending - " + elevatorInfo.toString());
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
    public void rpc_send(DatagramPacket request) {
        try {
            this.updateSocket.send(request);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(Thread.currentThread().getName() + ": Packet sent.\n");
    }



    //---------------------------------------------------------------------------


    /**
     * Starts the background update timer thread. This thread runs the update function once every UPDATE_RATE seconds
     */
    private void startUpdateTimer(){
        // Create TimerTask that will run once every UPDATE_RATE seconds
        TimerTask timerTask = new TimerTask() {
            private static long lastUpdateNanoseconds = System.nanoTime();
            @Override
            public void run() {
                long currTime = System.nanoTime();
                double elapsedTime = (double) (currTime - lastUpdateNanoseconds) / 1_000_000_000;
                update(elapsedTime);
                lastUpdateNanoseconds = currTime;
            }
        };
        updateTimer = new Timer();
        updateTimer.schedule(timerTask, 0, (long)(UPDATE_RATE * 1000));
    }

    /**
     * Update event that is run once every UPDATE_RATE seconds. This is called by the updateTimer
     * @param deltaT The amount of time elapsed since the last update, in milliseconds
     */
    public void update(double deltaT){
        // can probably call update function to elevators subcomponents like motor/door/button etc.
        // use deltaT for calculating how far the elevator should move etc
//        System.out.println("Elevator update! Last update was " + deltaT + " seconds ago");

    }

    /**
     * @return the current floor the Elevator is on.
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Move the elevator up one floor
     */
    public void moveUp(){
        if(currentFloor < 8){
            currentFloor += 1;
            System.out.println("Elevator moves up to floor " + currentFloor);
        }
    }

    /**
     * Move the elevator down one floor
     */
    public void moveDown(){
        if(currentFloor > 1){
            currentFloor -= 1;
            System.out.println("Elevator moves down to floor " + currentFloor);
        }
    }

//    /**
//     * Proccess request received from a given DatagramPacket
//     * @param requestPacket the DatagramPacket that was received. Should contain request data.
//     */
//    public void receiveRequest(DatagramPacket requestPacket){
//        // Process the received datagram.
//        System.out.println("ElevatorNetworkHandler: Received packet");
//        System.out.println("From host: " + requestPacket.getAddress());
//        System.out.println("Host port: " + requestPacket.getPort());
//        int len = requestPacket.getLength();
//        System.out.println("Length: " + len);
//        System.out.print("Containing: ");
//
//        System.out.println("received message string = " + new String(requestPacket.getData(), requestPacket.getOffset(), len));
//        System.out.println("received message bytes = " + Arrays.toString(requestPacket.getData()));
//
//        try {
//            Event event = getEventFromRequest(requestPacket);
//            System.out.println("Elevator received request from Scheduler from Floor " + event.getFloor() + " to go " + event.getFloorButton() +
//                    " to floor " + event.getCarButton() + ".");
//        } catch (Exception e) {
//
//        }
//    }

//    public Event getEventFromRequest (DatagramPacket requestPacket) throws IOException, ClassNotFoundException {
//        ByteArrayInputStream bais = new ByteArrayInputStream(requestPacket.getData(), requestPacket.getOffset(), requestPacket.getLength());
//        ObjectInputStream ois = new ObjectInputStream(bais);
//        return (Event)ois.readObject();
//    }

//    public Thread getNetworkHandler(){
//        return networkHandler;
//    }

    /**
     *Run the elevator thread
     */
    public void run(){
        while (true){
            if (hasRequest){
                try {
                    doRequest();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        }

    }

    public static void main(String[] args) {
        Thread elevator;
        elevator = new Thread(new  Elevator(3500));
        elevator.start();
    }
}
