package Subsystems;

import Data.Event;
import States.ElevatorButton;
import States.Door;
import States.Motor;

/**
 * The Elevator class simulates the behavior of an elevator car subsystem in the Elevator-Simulator project
 */
public class Elevator implements Runnable{

    private Scheduler schedulerSystem;
    private int currentFloor;
    private ElevatorButton[] elevatorButtons;
    private Door elevatorDoors;
    private Motor elevatorMotor;

    /**
     * Create an elevator that receive requests from the Scheduler
     * @param s Scheduler that controls this elevator and sending request
     */
    public Elevator(Scheduler s){
        this.schedulerSystem = s;
        this.currentFloor = 1;
        elevatorDoors = new Door();
        elevatorMotor = new Motor();

        elevatorButtons = new ElevatorButton[8];
        for(int i = 0; i < 8; i++){
            ElevatorButton newButton = new ElevatorButton(i+1);
            elevatorButtons[i] = newButton;
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
