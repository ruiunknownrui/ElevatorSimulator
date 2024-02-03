package Subsystems;

import Data.Event;

import java.util.ArrayList;

/**
 *The requestBuffer class is a thread-safe class for the Scheduler to save the requests received
 */
public class RequestBuffer {

    private ArrayList<Event> events;

    /**
     * Create a new RequestBuffer class
     */
    public RequestBuffer(){
        this.events = new ArrayList<>();
    }

    /**
     * Add new request into the RequestBuffer
     * @param e the new request to add
     */
    public synchronized void addToEvents(Event e){
        this.events.add(e);
        notifyAll();
    }

    /**
     * Return the next request stored in the RequestBuffer and remove it
     * @return the next request in the RequestBuffer
     */
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

    /**\
     * Check if the RequestBuffer is empty
     * @return true if there's no request in the requestBuffer;
     *         false otherwise
     */
    public boolean isEmpty() {
        return events.isEmpty();
    }
}

