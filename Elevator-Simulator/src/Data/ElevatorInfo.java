package Data;

import java.io.Serializable;

/**
 * ElevatorInfo is used to send the current elevator floor to the scheduler.
 */
public class ElevatorInfo implements Serializable {
    private int currFloor;  // Current floor

    /**
     * initialize the current floor of the object
     * @param currFloor  current floor
     */
    public ElevatorInfo(int currFloor){
        this.currFloor = currFloor;
    }

    /**
     * getCurrFloor returns the current floor information of the object
     * @return  the current floor information
     */
    public int getCurrFloor() {
        return currFloor;
    }
}
