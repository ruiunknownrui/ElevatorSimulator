package Subsystems;

import Data.Event;

import java.util.ArrayList;

/**
 * Scheduler class keep adding requests from floor to the buffer
 * @author Rebecca Li
 */
public class Scheduler implements Runnable{

    private ArrayList<Event> events;  // A list of all events received from floor
    private RequestBuffer requestBuffer;
    private static boolean floorDone;
    private boolean schedulerDone = false;


    // only used to test if all test cases are received, send and add
    // TODO: delete before submit
    private int sendI = 1;
    private int reI = 1;

    /**
     * Scheduler creates an empty event list
     * @param requestBuffer
     */
    public Scheduler(RequestBuffer requestBuffer){
        this.events = new ArrayList<>();
        this.requestBuffer = requestBuffer;
        floorDone = false;
    }

    /**
     * Set the state of variable floorDone when the floor has done its work
     */
    public static void floorDone() {
        floorDone = true;
    }

    /**
     * Check if all the transmissions have finished
     * @return true if all transmissions is done; false otherwise
     */
    public boolean notFinish(){
        if(requestBuffer.isEmpty() && floorDone) {
            schedulerDone = true;
            System.out.println("Scheduler done");
        }
        return (!schedulerDone);
    }

    /**
     * addEvent adds event to the event list
     * @param event
     */
    public void addEvent(Event event){
        System.out.print(reI + " - ");
        reI += 1;
        requestBuffer.addToEvents(event);
        System.out.println("Scheduler received request from floor " + event.getFloor() +
                " to go " + event.getFloorButton() +
                " to floor " + event.getCarButton() + ".");
    }

    /**
     * getEvents returns the list of event  (only used in unit test)
     * @return ArrayList
     */
    public ArrayList<Event> getEvents(){
        return events;
    }

    /**
     * replyWork returns uncompleted work.
     * @return  Event
     */
    public Event replyWork(){
       System.out.print(sendI + " - ");
        sendI += 1;
        Event work = requestBuffer.getNextEvent();  // Get event from the  buffer
        System.out.println("Scheduler sent work to Elevator " + work.getFloor() +
                " to go " + work.getFloorButton() + " to floor " + work.getCarButton() + ".");
        return work;
    }

    /**
     * Keep adding event in  the list to the buffer
     */
    @Override
    public void run() {
        while (notFinish()){
            if(requestBuffer.isEmpty() && floorDone) {
                schedulerDone = true;
                System.out.println("Scheduler done");
            }
        }
    }
}
