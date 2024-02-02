package Subsystems;

import Data.Event;
import States.SchedulerState;

public class Elevator implements Runnable{

    private Scheduler scheduler;
    private int currentFloor = 1;

    public Elevator(Scheduler s){ this.scheduler = s;}

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
        while (scheduler.getState() == SchedulerState.Active){
            //TODO: Process event and make scheduler inactive.

            Event data = scheduler.getNextEvent();

        }
    }
}
