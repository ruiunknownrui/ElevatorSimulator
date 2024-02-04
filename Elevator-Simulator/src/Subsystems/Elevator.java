package Subsystems;

import Data.Event;

/**
 * The Elevator class simulates the behavior of an elevator car subsystem in the Elevator-Simulator project
 */
public class Elevator implements Runnable{

    private Scheduler schedulerSystem;
    private int currentFloor;

    /**
     * Create an elevator that receive requests from the Scheduler
     * @param s Scheduler that controls this elevator and sending request
     */
    public Elevator(Scheduler s){
        this.schedulerSystem = s;
        this.currentFloor = 1;
    }

    /**
     * Move the elevator uo one floor
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
        while (schedulerSystem.notFinish()){
            System.out.println("call nf from E");
            //TODO: Process event and make scheduler inactive.
            Event e = schedulerSystem.replyWork();
            System.out.println("Elevator received request from Scheduler " + e.getFloor() + " to go " + e.getFloorButton() +
                    " to floor " + e.getCarButton() + ".");
           try{
                Thread.sleep(400);
            } catch (InterruptedException ignored) {}
        }
    }
}
