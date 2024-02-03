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
//    private boolean hasRequest = true;

    // only used to test if all test cases are received, send and add
    // TODO: delete before submit
    private int addI = 1;
    private int sendI = 1;
    private int reI = 1;

    /**
     * Scheduler creates an empty event list
     * @param requestBuffer
     */
    public Scheduler(RequestBuffer requestBuffer){
        this.events = new ArrayList<>();
        this.requestBuffer = requestBuffer;
    }

//    private void updateHasRequest() {
//        if (requestBuffer.getFloorDone() && events.isEmpty()){
//            hasRequest = false;
//        }
//    }
//
//    public boolean getHasRequest(){
//        return hasRequest;
//    }

    /**
     * addEvent adds event to the event list
     * @param event
     */
    public void addEvent(Event event){
        events.add(event);
        System.out.print(reI + " - ");
        reI += 1;
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
        Event work = requestBuffer.getNextEvent();  // Get event from the  buffer
        System.out.print(sendI + " - ");
        sendI += 1;
        System.out.println("Scheduler sent work to Elevator " + work.getFloor() +
                " to go " + work.getFloorButton() + " to floor " + work.getCarButton() + ".");
//        updateHasRequest();
        return work;
    }

    /**
     * Keep adding event in  the list to the buffer
     */
    @Override
    public void run() {
        while (true){
            if(!events.isEmpty()){  // If there is request in the scheduler
                Event firstEvent = events.getFirst();  // Gets the first request in the list
                requestBuffer.addToEvents(firstEvent);  // Adds request to the buffer
                System.out.print(addI + " - ");
                addI += 1;
                System.out.println("Scheduler add request to buffer " + firstEvent.getFloor() +
                        " to go " + firstEvent.getFloorButton() + " to floor " + firstEvent.getCarButton() + ".");
                events.remove(firstEvent);  // Removes the request from the list
            }
            try{
                Thread.sleep(400);
            } catch (InterruptedException ignored) {}
        }
    }
}
