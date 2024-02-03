package Subsystems;

import Data.Event;

import java.util.ArrayList;

public class RequestBuffer {

    private boolean readable;
    private ArrayList<Event> events;
    private static boolean floorDone;
    private boolean schedulerDone = false;

    public RequestBuffer(){
        this.readable = false;
        this.events = new ArrayList<>();
        floorDone = false;
    }

    public static void floorDone() {
        floorDone = true;
    }

    public boolean notFinish(){
        if(this.events.isEmpty() && floorDone) {
            schedulerDone = true;
        }
        return (!floorDone || !schedulerDone);
    }

    public synchronized void addToEvents(Event e){
        System.out.println("Scheduler received request from floor " + e.getFloor() + " to go " + e.getFloorButton() +
                " to floor " + e.getCarButton() + ".");
        this.events.add(e);
        notifyAll();
        try{
            Thread.sleep(400);
        } catch (InterruptedException ignored) {}
    }

    public synchronized Event getNextEvent(){
        while(events.isEmpty() && !schedulerDone){
            try{
                wait();
            } catch (InterruptedException e){
                System.err.println(e);
            }
        }

        Event eV = this.events.removeFirst();
        notifyAll();
        return eV;
    }
}

