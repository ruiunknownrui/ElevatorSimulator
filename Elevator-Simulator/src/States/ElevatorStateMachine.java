package States;

public class ElevatorStateMachine {

    private enum ElevatorStates {
        IDLE,
        ACCELERATING,
        MOVING_AT_MAX_VELOCITY,
        DECELERATING,
        ARRIVED,
        DOORS_OPENING,
        DOORS_OPEN,
        DOORS_CLOSING,
        DOORS_CLOSED,
        ERROR
    }

    private ElevatorStates currentState;

    public ElevatorStateMachine(){ currentState = ElevatorStates.IDLE; }


}
