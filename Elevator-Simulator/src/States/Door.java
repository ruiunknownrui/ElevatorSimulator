package States;

public class Door {

    public enum DoorStates {
        DOORS_OPENING,
        DOORS_OPEN,
        DOORS_CLOSING,
        DOORS_CLOSED,
    }

    private DoorStates currDoorState;

    public Door(){ this.currDoorState = DoorStates.DOORS_CLOSED; }

    public void operateDoors(){
        switch (currDoorState){
            case DOORS_CLOSED -> currDoorState = DoorStates.DOORS_OPENING;
            case DOORS_OPENING -> currDoorState = DoorStates.DOORS_OPEN;
            case DOORS_OPEN -> currDoorState = DoorStates.DOORS_CLOSING;
            case DOORS_CLOSING -> currDoorState = DoorStates.DOORS_CLOSED;
        }
    }
}
