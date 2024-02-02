package Subsystems;

import Data.Event;
import States.SchedulerState;

public class Elevator implements Runnable{

    private Scheduler scheduler;
    private int currentFloor;

    public Elevator(Scheduler s){
        this.scheduler = s;
        this.currentFloor = 1;
    }

    public void moveUp(){
        if(currentFloor < 8){
            currentFloor += 1;
            System.out.println("Elevator moves up to floor " + currentFloor);
        }
    }

    public void moveDown(){
        if(currentFloor > 1){
            currentFloor -= 1;
            System.out.println("Elevator moves down to floor " + currentFloor);
        }
    }

    public void run(){
        Event data = scheduler.getNextEvent();
        while(data != null) {
            System.out.println("Elevator received data from Scheduler.");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { return; }
            data = scheduler.getNextEvent();
        }
    }
}
