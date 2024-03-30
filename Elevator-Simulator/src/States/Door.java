package States;

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
    private int doingTime;

    public Door(int doingTime){ this.currDoorState = DoorStates.DOORS_OPEN; this.doingTime = doingTime;}

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
     */
    public void controlDoor() {
        if (this.currDoorState == DoorStates.DOORS_OPEN || this.currDoorState == DoorStates.DOORS_CLOSED) {
            System.out.println("Door: " + this.currDoorState);
            this.operateDoors();
            System.out.println("Door: " + this.currDoorState);
            try {
                Thread.sleep(this.doingTime);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            this.operateDoors();
            System.out.println("Door: " + this.currDoorState);
        }
    }
}
