package Subsystems;

public class ArrivalSensor {
    private final Elevator elevator;
    private final int floorNum;
    private boolean triggered;

    ArrivalSensor(Elevator e, int floor){
        elevator = e;
        floorNum = floor;
        triggered = false;
    }

    public boolean isTriggered(){
        return triggered;
    }

    private void triggerSensor(){
        // TODO: Call function in elevator to notify of floor change, just setting floor number for now
        if (!triggered){
            elevator.setCurrentFloor(floorNum);
            triggered = true;
        }
    }

    public void resetSensor(){
        triggered = false;
    }

    public void checkSensor(){
        // TODO: Change check criteria, use direct elevator position if implemented
        if (elevator.getCurrentFloor() == floorNum){
            triggerSensor();
        }
    }
}
