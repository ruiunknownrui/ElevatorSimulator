package Data;

import java.io.Serializable;

/**
 * ElevatorInfo is used to send the current elevator floor to the scheduler.
 */
public class ElevatorInfo implements Serializable {
    private int currFloor;  // Current floor
    private boolean doingRequest;  // If the elevator doing request
    private boolean isShutDown;

    /**
     * initialize the current floor of the object
     * @param currFloor  current floor
     */
    public ElevatorInfo(int currFloor, boolean doingRequest, boolean isShutDown){
        this.currFloor = currFloor;
        this.doingRequest = doingRequest;
        this.isShutDown = isShutDown;
    }

    /**
     * getCurrFloor returns the current floor information of the object
     * @return  the current floor information
     */
    public int getCurrFloor() {
        return currFloor;
    }

    /**
     * isDoingRequest returns if the elevator is doing request
     * @return
     */
    public boolean isDoingRequest(){
        return doingRequest;
    }

    /**
     * isShutDown returns if the elevator has shut down
     * @return
     */
    public boolean isShutDown(){
        return isShutDown;
    }

    /**
     * toString returns the String format of the Elevator Information
     * @return
     */
    public String toString(){
        return "Current floor: " + this.currFloor + " Doing Request: " + this.doingRequest;
    }
}
