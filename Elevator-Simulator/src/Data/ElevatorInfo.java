package Data;

import java.io.Serializable;

/**
 * ElevatorInfo is used to send the current elevator floor to the scheduler.
 */
public class ElevatorInfo implements Serializable {
    private int currFloor;  // Current floor
    private boolean doingRequest;  // If the elevator doing request

    /**
     * initialize the current floor of the object
     * @param currFloor  current floor
     */
    public ElevatorInfo(int currFloor, boolean doingRequest){
        this.currFloor = currFloor;
        this.doingRequest = doingRequest;
    }

    /**
     * getCurrFloor returns the current floor information of the object
     * @return  the current floor information
     */
    public int getCurrFloor() {
        return currFloor;
    }

    /**
     * toString returns the String format of the Elevator Information
     * @return
     */
    public String toString(){
        return "Current floor: " + this.currFloor + " Doing Request: " + this.doingRequest;
    }
}
