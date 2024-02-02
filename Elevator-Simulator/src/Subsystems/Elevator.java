package Subsystems;

import States.SchedulerState;

public class Elevator implements Runnable{

    private Scheduler scheduler;
    private int currentFloor = 1;

    public Elevator(Scheduler s){ this.scheduler = s;}

    public void run(){
        while (scheduler.getState() == SchedulerState.Active){

        }
    }
}
