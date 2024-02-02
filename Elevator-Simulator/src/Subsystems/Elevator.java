package Subsystems;

import States.SchedulerState;

public class Elevator implements Runnable{

    Scheduler scheduler;

    public Elevator(Scheduler s){ this.scheduler = s;}

    public void run(){
        while (scheduler.getState() == SchedulerState.Active){

        }
    }
}
