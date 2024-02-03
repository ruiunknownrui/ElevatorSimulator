package Subsystems;

import Data.Event;

import java.util.ArrayList;

public class RequestBuffer {

    private ArrayList<Event> events;
    public RequestBuffer(){
        this.events = new ArrayList<>();
    }


    public synchronized void addToEvents(Event e){
        this.events.add(e);
        notifyAll();
        try{
            Thread.sleep(300);
        } catch (InterruptedException ignored) {}
    }

    public synchronized Event getNextEvent(){
        while(events.isEmpty()){
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

    public boolean isEmpty() {
        return events.isEmpty();
    }
}

