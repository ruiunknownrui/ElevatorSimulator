package Subsystems;

import Data.Ack;
import Data.ElevatorInfo;
import Data.Event;
import Data.FaultConstant;
import States.ElevatorButton;
import States.Door;
import States.Motor;
import View.ElevatorView;
import View.SystemView;

import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Elevator class simulates the behavior of an elevator car subsystem in the Elevator-Simulator project
 */
public class Elevator implements Runnable{


    private Thread networkHandler;
    private DatagramSocket updateSocket;  // The socket only used to update the elevator arrival information
    private Event previousReceive = null;
    private int previousFloor = -1;
    private int currentFloor;
    private int targetFloor;  // destination of the request
    private int nextFloor;  // start floor of the request
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private boolean hasRequest = false;
    private boolean receiveNewRequest = false;
    private ElevatorButton[] elevatorButtons;
    private Door elevatorDoors;
    private Motor elevatorMotor;
    private Timer updateTimer;
    private static final double UPDATE_RATE = (double) 1/30; // Amount of seconds in between each update

    private int currPort;
    private FaultConstant.Fault inputFault;
    private boolean elevatorShutDown = false;

    private ElevatorView displayView;
    // Assume only one person takes the elevator, the boarding/unboarding tims is 5 seconds -> 5000 milliseconds
    private final int boardingTime = 5000;

    private double curretnTime = 0.0;

    /**
     * Create an elevator that receive requests from the Scheduler
     */
    public Elevator(int port, ElevatorView displayView){
        //TODO: Change constructor to use port for network handler instead of directly using scheduler
        this.displayView = displayView;

        this.currentFloor = 1;
        this.displayView.updateDescription("Current Floor: " + this.currentFloor);
        this.displayView.updateFloor(this.previousFloor, this.currentFloor);
        elevatorDoors = new Door(displayView);
        elevatorMotor = new Motor(this);

        elevatorButtons = new ElevatorButton[8];
        for(int i = 0; i < 8; i++){
            ElevatorButton newButton = new ElevatorButton(i+1);
            elevatorButtons[i] = newButton;
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
        if (this.previousReceive == null || this.previousReceive != event) {
            this.previousReceive = event;
            nextFloor = event.getFloor();
            targetFloor = event.getCarButton();
            receiveNewRequest = true;
            hasRequest = true;
            inputFault = event.getFault();
        }
        notifyAll();
    }

    public String getCurTime(){
        Calendar currentTime = Calendar.getInstance();
        return currentTime.get(Calendar.HOUR) + ":" +
                currentTime.get(Calendar.MINUTE) + ":" + currentTime.get(Calendar.SECOND);
    }

    public synchronized void doRequest() throws IOException {
        while(!this.hasRequest && !this.elevatorShutDown && receiveNewRequest){
            try{
                wait();
            } catch (InterruptedException e){
                System.err.println(e);
            }
        }

        this.sendAndReceive();  // Tell scheduler this elevator has request

        this.displayView.updateDescription("\n Received Request - " + this.getCurTime());

        // If the current floor is the one the elevator needs to take the passenger
        if (this.nextFloor != this.currentFloor){
            // This should always be true, just double check
            if(this.elevatorDoors.getCurrDoorState() == Door.DoorStates.DOORS_OPEN){
                this.elevatorDoors.controlDoor(this.currPort, false, this.currentFloor);  // close elevator doors
                this.moveElevator(this.nextFloor, false, false);  // Move elevator to the start position
                this.elevatorDoors.controlDoor(this.currPort, false, this.currentFloor);  // open elevator doors
            }else {
                System.out.println("DOOR STATE ARE INCORRECT!!!!!!!");
            }
        }
        // This should always be true, just double check
        if(this.elevatorDoors.getCurrDoorState() == Door.DoorStates.DOORS_OPEN){
            this.boardPassenger(true);  // stimulates passenger moving into the elevator
            boolean detectFault;
            detectFault = this.inputFault == FaultConstant.Fault.DOOR_STUCK_OPEN;
            this.elevatorDoors.controlDoor(this.currPort, detectFault, this.currentFloor);  // close door
        }else {
            System.out.println("DOOR STATE ARE INCORRECT!!!!!!!");
        }
        boolean hasElevatorFault = this.inputFault == FaultConstant.Fault.ELEVATOR_STUCK;
        // move to destination
        this.moveElevator(this.targetFloor, true, hasElevatorFault);

        if (!hasElevatorFault) {
            if (this.elevatorDoors.getCurrDoorState() == Door.DoorStates.DOORS_CLOSED) {  // open the door after arrive
                this.elevatorDoors.controlDoor(this.currPort, false, this.currentFloor);  // open door
            }

            this.boardPassenger(false);  // stimulates passenger move out
        }

//        hasRequest = false;
//        this.displayView.updateDescription("has request at the end of do request: " +  this.hasRequest);
//        this.displayView.updateDescription("receive request at the end of do request: " +  this.receiveNewRequest);
        notifyAll();
    }

    /**
     * boardPassenger stimulate that the elevator is boarding/unboarding the passenger
     * @param isMovingIn  if the passenger is moving in the elevator or moving out
     */
    public void boardPassenger(boolean isMovingIn){
        if (isMovingIn){
            System.out.println(this.currPort + " Passenger is moving in.");
            this.displayView.updateDescription(this.getCurTime() + ": Passenger is moving in.");
        }else {
            System.out.println(this.currPort + " Passenger is moving out.");
            this.displayView.updateDescription(this.getCurTime() + ": Passenger is moving out.");
        }
        try {
            Thread.sleep(this.boardingTime);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        if (isMovingIn){
            System.out.println(this.currPort + " Passenger moved in.");
            this.displayView.updateDescription(this.getCurTime() + ": Passenger moved in.");
        }else {
            System.out.println(this.currPort + " Passenger moved out.");
            this.displayView.updateDescription(this.getCurTime() + ": Passenger moved out.");
        }
    }

    public void moveElevator(int targetFloor, boolean isRequestDestination, boolean hasFault) throws IOException {
        while(targetFloor > this.currentFloor){
            this.previousFloor = this.currentFloor;
            this.currentFloor += 1;
            this.elevatorMotor.elevatorMoving();
            if (isRequestDestination && targetFloor == currentFloor && !hasFault){
                this.hasRequest = false;
            } else if (hasFault) {
                break;
            }
            this.displayView.updateFloor(this.previousFloor, this.currentFloor);
            this.displayView.updateDescription(this.getCurTime() + ": Current Floor: " + currentFloor);
            sendAndReceive();
        }
        while(targetFloor < currentFloor){
            this.previousFloor = this.currentFloor;
            this.currentFloor -= 1;
            this.elevatorMotor.elevatorMoving();
            this.displayView.updateDescription(this.getCurTime() + ": Current Floor: " + currentFloor);
            this.displayView.updateFloor(this.previousFloor, this.currentFloor);
            if (isRequestDestination && targetFloor == currentFloor  && !hasFault){
                this.hasRequest = false;
            }else if (hasFault) {
                break;
            }
            sendAndReceive();
        }

        if (hasFault){
            System.out.println("!!!!! " + currPort + " Elevator Stuck !!!!!");
            this.displayView.addFaultDescription(this.getCurTime() + ": Elevator Stuck!!!");
            this.elevatorShutDown = true;  // shut down the elevator
            System.out.println( "!!!!!!!!!! " + currPort + " Fix Elevator Stuck - Elevator Shut Down: " +
                    this.elevatorShutDown + "!!!!!!!!!!");
            this.displayView.updateDescription(this.getCurTime() + ": Elevator Shut Down!");
            this.displayView.displayFault(this.currentFloor, false);
            hasRequest = true;
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
        this.curretnTime = deltaT;

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
            if (receiveNewRequest && !elevatorShutDown){
                receiveNewRequest = false;
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
        Thread elevator1, elevator2, elevator3, elevator4;
        ElevatorView view1, view2, view3, view4;
        SystemView view = new SystemView();

        view1 = view.getElevatorView(0);
        view1.setElevatorName("Elevator 1");
        elevator1 = new Thread(new  Elevator(3500, view1));
        elevator1.start();

        view2 = view.getElevatorView(1);
        view2.setElevatorName("Elevator 2");
        elevator2 = new Thread(new  Elevator(3510, view2));
        elevator2.start();

        view3 = view.getElevatorView(2);
        view3.setElevatorName("Elevator 3");
        elevator3 = new Thread(new  Elevator(3520, view3));
        elevator3.start();

        view4 = view.getElevatorView(3);
        view4.setElevatorName("Elevator 4");
        elevator4 = new Thread(new  Elevator(3530, view4));
        elevator4.start();
    }
}
