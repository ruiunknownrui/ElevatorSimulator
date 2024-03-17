package Data;

import Subsystems.Elevator;

public class ElevatorInfo {
    private int currFloor;  // Current floor

    public ElevatorInfo(int currFloor){
        this.currFloor = currFloor;
    }

    public int getCurrFloor() {
        return currFloor;
    }
}
