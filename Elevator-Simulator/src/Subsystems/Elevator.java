package Subsystems;

import Data.Event;
import States.ElevatorButton;
import States.Door;
import States.Motor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Elevator class simulates the behavior of an elevator car subsystem in the Elevator-Simulator project
 */
public class Elevator implements Runnable{

    private Scheduler schedulerSystem;
    private Thread networkHandler;
    private static int nextPort = 4000; //TODO: Change constructor to use port for network handler instead of directly using scheduler
    private int port;
    private int currentFloor;
    private ElevatorButton[] elevatorButtons;
    private Door elevatorDoors;
    private Motor elevatorMotor;
    private Timer updateTimer;
    private static final double UPDATE_RATE = (double) 1/30; // Amount of seconds in between each update

    /**
     * Create an elevator that receive requests from the Scheduler
     * @param s Scheduler that controls this elevator and sending request
     */
    public Elevator(Scheduler s){
        //TODO: Change constructor to use port for network handler instead of directly using scheduler
        this.schedulerSystem = s;
        this.currentFloor = 1;
        elevatorDoors = new Door();
        elevatorMotor = new Motor(this);

        elevatorButtons = new ElevatorButton[8];
        for(int i = 0; i < 8; i++){
            ElevatorButton newButton = new ElevatorButton(i+1);
            elevatorButtons[i] = newButton;
        }

        // setup network handler
        port = nextPort++;
        try {
            networkHandler = new Thread(new ElevatorNetworkHandler(this, port));
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        networkHandler.start();
        startUpdateTimer();
    }

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
        System.out.println("Elevator update! Last update was " + deltaT + " seconds ago");
    }

    /**
     * @return the current floor the Elevator is on.
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * @return the elevator's network port
     */
    public int getPort(){
        return port;
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
     * Proccess request received from a given DatagramPacket
     * @param requestPacket the DatagramPacket that was received. Should contain request data.
     */
    public void receiveRequest(DatagramPacket requestPacket){
        // Process the received datagram.
        System.out.println("ElevatorNetworkHandler: Received packet");
        System.out.println("From host: " + requestPacket.getAddress());
        System.out.println("Host port: " + requestPacket.getPort());
        int len = requestPacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");

        System.out.println("received message string = " + new String(requestPacket.getData(), requestPacket.getOffset(), len));
        System.out.println("received message bytes = " + Arrays.toString(requestPacket.getData()));

        try {
            Event event = getEventFromRequest(requestPacket);
            System.out.println("Elevator received request from Scheduler from Floor " + event.getFloor() + " to go " + event.getFloorButton() +
                    " to floor " + event.getCarButton() + ".");
        } catch (Exception e) {

        }
    }

    public Event getEventFromRequest (DatagramPacket requestPacket) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(requestPacket.getData(), requestPacket.getOffset(), requestPacket.getLength());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Event)ois.readObject();
    }

    /**
     *Run the elevator thread
     */
    public void run(){
        while (schedulerSystem.keepSending()){
            Event e = schedulerSystem.replyWork();
            System.out.println("Elevator received request from Scheduler from Floor " + e.getFloor() + " to go " + e.getFloorButton() +
                    " to floor " + e.getCarButton() + ".");

            try{
                Thread.sleep(400);
            } catch (InterruptedException ignored) {}
        }
        System.out.println("Elevator done.");
    }
}
