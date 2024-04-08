package States;

import Subsystems.Elevator;
import View.ElevatorView;

/**
 * Class for the state machine of the Door subcomponent of the Elevator
 */
public class Door {

    /**
     * Different states for the Door as enum values: DOORS_OPENING, DOORS_OPEN, DOORS_CLOSING, DOORS_CLOSED.
     */
    public enum DoorStates {
        DOORS_OPENING,
        DOORS_OPEN,
        DOORS_CLOSING,
        DOORS_CLOSED,
    }

    private DoorStates currDoorState;
    private ElevatorView thisElevatorView;
    private final int doingTime = 3000;  // 3 seconds -> 3000 milliseconds
    private final int solvingStuckTime = 20000;  // 20 seconds -> 20000 milliseconds

    public Door(ElevatorView thisElevatorView){
        this.currDoorState = DoorStates.DOORS_OPEN; this.thisElevatorView = thisElevatorView;
    }

    /**
     * Function to change state of doors according to state machine
     */
    public void operateDoors(){
        switch (currDoorState){
            case DOORS_CLOSED:
                currDoorState = DoorStates.DOORS_OPENING;
                System.out.println("Elevator Door State: Doors Opening");
                break;
            case DOORS_OPENING:
                currDoorState = DoorStates.DOORS_OPEN;
                System.out.println("Elevator Door State: Doors Open");
                break;
            case DOORS_OPEN:
                currDoorState = DoorStates.DOORS_CLOSING;
                System.out.println("Elevator Door State: Doors Closing");
                break;
            case DOORS_CLOSING:
                currDoorState = DoorStates.DOORS_CLOSED;
                System.out.println("Elevator Door State: Doors Closed");
                break;
        }
    }

    /**
     * Function to access the current state of the Door
     * @return Current Door state
     */
    public DoorStates getCurrDoorState(){ return this.currDoorState; }

    /**
     * controlDoor opens or close door
     * @param elevatorPort  the port of the corresponding elevator
     * @param hasFault  if fault happens during opening/closing
     */
    public void controlDoor(int elevatorPort, boolean hasFault, int currFloor) {
        if (this.currDoorState == DoorStates.DOORS_OPEN || this.currDoorState == DoorStates.DOORS_CLOSED) {
            System.out.println(elevatorPort + " Door: " + this.currDoorState);
            this.operateDoors();  // door opening or closing
            System.out.println(elevatorPort + " Door: " + this.currDoorState);
            this.thisElevatorView.updateDescription("Door " + this.currDoorState);
            try {
                Thread.sleep(this.doingTime);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if (hasFault){  // If has fault, wait for 20 seconds to fix
                System.out.println("!!!!! " +  elevatorPort + " Door stuck: " + this.currDoorState + "!!!!!");
                this.thisElevatorView.addFaultDescription("Door Stuck!");
                this.thisElevatorView.displayFault(currFloor, false);
                try {
                    Thread.sleep(this.solvingStuckTime);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("!!!!! " +  elevatorPort + " Door stuck solved: " + this.currDoorState + "!!!!!");
                this.thisElevatorView.addFaultDescription("Door Stuck Fixed!");
                this.thisElevatorView.displayFault(currFloor, true);
            }
            this.operateDoors();
            System.out.println(elevatorPort + " Door: " + this.currDoorState);
            this.thisElevatorView.updateDescription("Door " + this.currDoorState);
        }
    }
}
