package Subsystems;

import Data.Ack;
import Data.ElevatorInfo;
import Data.Event;
import Data.FaultConstant;
import States.ElevatorButton;
import States.Door;
import States.Motor;

import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Elevator class simulates the behavior of an elevator car subsystem in the Elevator-Simulator project
 */
public class Elevator implements Runnable{


    private Thread networkHandler;
    private DatagramSocket updateSocket;  // The socket only used to update the elevator arrival information

    public void setCurrentFloor(int newFloor) {
        if (newFloor <= 0 || newFloor > arrivalSensors.length) throw new IllegalArgumentException("Floor number invalid!");
        // resets arrival sensor for current floor
        if (newFloor != currentFloor){
            arrivalSensors[currentFloor - 1].resetSensor();
            this.currentFloor = newFloor;
        }
    }

    private int currentFloor;
    private int targetFloor;  // destination of the request
    private int nextFloor;  // start floor of the request
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private boolean hasRequest = false;
    private ElevatorButton[] elevatorButtons;
    private ArrivalSensor[] arrivalSensors;
    private Door elevatorDoors;
    private Motor elevatorMotor;
    private Timer updateTimer;
    private static final double UPDATE_RATE = (double) 1/30; // Amount of seconds in between each update
    // to make calculation easy,
    // assume the elevator needs 2 seconds to move up or down each floor.
    // Assume the elevator needs 1 second to open or close the door.
    private final int doorTime = 1;
    private final int moveTime = 2;

    private int currPort;
    private FaultConstant.Fault inputFault;
    private boolean elevatorShutDown = false;

    /**
     * Create an elevator that receive requests from the Scheduler
     */
    public Elevator(int port){
        //TODO: Change constructor to use port for network handler instead of directly using scheduler
        this.currentFloor = 1;;
        elevatorDoors = new Door(this.doorTime);
        elevatorMotor = new Motor();

        elevatorButtons = new ElevatorButton[8];
        arrivalSensors = new ArrivalSensor[8];
        for(int i = 0; i < 8; i++){
            ElevatorButton newButton = new ElevatorButton(i+1);
            elevatorButtons[i] = newButton;
            arrivalSensors[i] = new ArrivalSensor(this, i + 1);
        }

        this.currPort = port;
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
        inputFault = event.getFault();
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
        // If elevator door is opened before move
        if (this.elevatorDoors.getCurrDoorState() == Door.DoorStates.DOORS_OPEN){
            this.elevatorDoors.controlDoor(this.currPort, false);  // close door
        }
        moveElevator(this.nextFloor, false);  // Move elevator to the start position
        // If input fault is elevator fault, assume is stuck before reach to passengers
        if (inputFault == FaultConstant.Fault.ELEVATOR_STUCK){
            System.out.println("!!!!! " + currPort + " Elevator Stuck !!!!!");
            this.elevatorShutDown = true;  // shut down the elevator
            System.out.println("!!!!!!!!!! " + currPort + " Fix Elevator Stuck - Elevator Shut Down: " +
                    this.elevatorShutDown + "!!!!!!!!!!");
            hasRequest = false;
            notifyAll();
            return;
        }
        if (this.elevatorDoors.getCurrDoorState() == Door.DoorStates.DOORS_CLOSED){  // open the door after arrive
            this.elevatorDoors.controlDoor(this.currPort, false);  // open door
        }
        if (this.elevatorDoors.getCurrDoorState() == Door.DoorStates.DOORS_OPEN){  // close the door (assume passenger
            boolean detectFault;
            if (this.inputFault == FaultConstant.Fault.DOOR_STUCK_OPEN) {
                detectFault = true;
            }else {
                detectFault = false;
            }
            this.elevatorDoors.controlDoor(this.currPort, detectFault);  // close door
            if (detectFault){  // if has door fault, fix the door fault
                this.elevatorDoors.operateDoors();
                System.out.println("!!!!!!!!!! " + currPort + " Door stuck fix - Current Door State: " +
                        this.elevatorDoors.getCurrDoorState() + " !!!!!!!!!!");
            }
        }
        moveElevator(this.targetFloor, true);  // move to destination
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


    public void moveElevator(int targetFloor, boolean isRequestDestination) throws IOException {
        while(targetFloor > currentFloor){
            currentFloor += 1;
            try {
                Thread.sleep(this.moveTime);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if (isRequestDestination && targetFloor == currentFloor){
                this.hasRequest = false;
            }
            sendAndReceive();
        }
        while(targetFloor < currentFloor){
            currentFloor -= 1;
            try {
                Thread.sleep(this.moveTime);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if (isRequestDestination && targetFloor == currentFloor){
                this.hasRequest = false;
            }
            sendAndReceive();
        }
    }

    /**
     * Sends a message to the scheduler and receive acknowledge
     */
    public void sendAndReceive() throws IOException {

        ElevatorInfo elevatorInfo = new ElevatorInfo(this.currentFloor, this.hasRequest, this.elevatorShutDown);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOut);
        output.writeObject(elevatorInfo);
        byte[] sendMsg = byteOut.toByteArray();

        try {
            this.sendPacket = new DatagramPacket(sendMsg, sendMsg.length, InetAddress.getLocalHost(), 3002);
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
                    " - Sending - " + elevatorInfo.toString() + " From: " + this.currPort);
            rpc_send(sendPacket);

            try {
                this.updateSocket.receive(receivePacket);  // Attempt to receive the acknowledgment
                this.handleAcknowledgment(receivePacket);  // Handle the acknowledgment
                receivedResponse = true;
            } catch (SocketTimeoutException ste) {
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

        for (int i = 0; i < arrivalSensors.length; i++) {
            arrivalSensors[i].checkSensor();
        }
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
        Thread elevator1, elevator2;
        elevator1 = new Thread(new  Elevator(3500));
        elevator1.start();
        elevator2 = new Thread(new  Elevator(3510));
        elevator2.start();
    }
}
