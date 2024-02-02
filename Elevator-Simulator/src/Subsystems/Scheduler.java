package Subsystems;

import States.SchedulerState;

public class Scheduler {

    private SchedulerState state;

    public Scheduler(){
        this.state = SchedulerState.Active;
    }

    public SchedulerState getState(){ return this.state; }

    public synchronized void handleEvents(){

    }
}

