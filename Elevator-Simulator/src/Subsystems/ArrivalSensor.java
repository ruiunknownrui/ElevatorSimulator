package Subsystems;

public class ArrivalSensor {
    private Elevator elevator;
    private int floorNum;
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
        elevator.setCurrentFloor(floorNum);
        triggered = true;
    }

    public void resetSensor(){
        triggered = false;
    }

    private void checkSensor(){
        // TODO: Change check criteria to use direct elevator position maybe
        if (elevator.getCurrentFloor() == floorNum){
            triggerSensor();
        }
    }
}
