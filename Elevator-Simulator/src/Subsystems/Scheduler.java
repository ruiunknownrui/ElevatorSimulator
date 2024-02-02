package Subsystems;

import Data.Event;
import States.SchedulerState;

import java.util.ArrayList;

public class Scheduler {

    private boolean readable;
    private ArrayList<Event> events;
    private boolean hasWork;

    public Scheduler(){
        this.readable = false;
        this.events = new ArrayList<>();
        this.hasWork = false;
    }

    public boolean hasWork(){ return this.hasWork; }

    public synchronized void addToEvents(Event e){
        while(this.hasWork()){
            try{
                wait();
            } catch (InterruptedException ie){
                System.err.println(ie);
            }
        }
        System.out.println("Scheduler received request from floor " + e.getFloor() + " to go " + e.getFloorButton() +
                " to floor " + e.getCarButton() + ".");
        this.events.add(e);
        this.hasWork = true;
        notifyAll();
    }

    public synchronized Event getNextEvent(){
        while(!this.hasWork()){
            try{
                wait();
            } catch (InterruptedException e){
                System.err.println(e);
            }
        }

        Event eV = this.events.removeFirst();
        if(this.events.isEmpty()){
            this.hasWork = false;
        }

        notifyAll();
        return eV;
    }
}

