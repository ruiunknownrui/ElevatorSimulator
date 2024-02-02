package Subsystems;

import Data.Event;
import States.SchedulerState;

import java.util.ArrayList;

public class Scheduler {

    private SchedulerState state;
    private boolean readable = false;
    private ArrayList<Event> events;

    public Scheduler(){ this.state = SchedulerState.Active; }
    public SchedulerState getState(){ return this.state; }

    public synchronized void addToEvents(Event e){
        while(readable){
            try {
                wait();
            } catch (InterruptedException ie) {
                return;
            }
        }
        System.out.println("Scheduler received request from Floor " + e.getFloor() + " to go " + e.getFloorButton() +
                " to floor " + e.getCarButton() + ".");
        this.events.add(e);
        notifyAll();
    }

    public synchronized Event getNextEvent(){
        return this.events.removeFirst();
    }
}

