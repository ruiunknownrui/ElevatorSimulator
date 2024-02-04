package Subsystems;

import Data.Event;


/**
 * Scheduler class keep adding requests from floor to the buffer
 * @author Rebecca Li
 */
public class Scheduler implements Runnable{

    private RequestBuffer requestBuffer;
    private static boolean floorDone;
    private int totalRequest;
    private int numOfSend;


    /**
     * Scheduler creates an empty event list
     * @param requestBuffer
     */
    public Scheduler(RequestBuffer requestBuffer){
        this.requestBuffer = requestBuffer;
        floorDone = false;
        totalRequest = 0;
        numOfSend = 0;
    }

    /**
     * Set the state of variable floorDone when the floor has done its work
     */
    public static void floorDone() {
        floorDone = true;
    }

    /**
     * setTotalRequest sets the total number of requests sent from floor
     * @param numOfTotal  the total number of requests
     */
    public void setTotalRequest(int numOfTotal){
        totalRequest = numOfTotal;
        System.out.println("total request: " + totalRequest);
    }

    /**
     * getTotalRequest returns the total number of requests received from the floor (Only used in Unit Test)
     * @return  total number of request
     */
    public int getTotalRequest(){
        return totalRequest;
    }

    /**
     * keepSending indicate if the thread is good enough to stop
     * @return   true if floor finish sending all requests and all requests are sent to elevator
     */
    public boolean keepSending(){
        return !(floorDone && totalRequest == numOfSend);
    }

    /**
     * addEvent adds event to the event list
     * @param event
     */
    public void addEvent(Event event){
        requestBuffer.addToEvents(event);
        System.out.println("Scheduler received request from floor " + event.getFloor() +
                " to go " + event.getFloorButton() +
                " to floor " + event.getCarButton() + ".");
    }

    /**
     * replyWork returns uncompleted work.
     * @return  Event
     */
    public Event replyWork(){
        numOfSend += 1;
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
        while (keepSending()){
            try{
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
        System.out.println("Scheduler done");
    }
}
