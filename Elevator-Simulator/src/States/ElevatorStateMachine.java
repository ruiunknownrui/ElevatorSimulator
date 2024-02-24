package States;

public class ElevatorStateMachine {

    private enum ElevatorMotorStates {
        IDLE,
        ACCELERATING,
        MOVING_AT_MAX_VELOCITY,
        DECELERATING,
        ARRIVED,
        ERROR
    }

    public enum ElevatorDoorStates {
        DOORS_OPENING,
        DOORS_OPEN,
        DOORS_CLOSING,
        DOORS_CLOSED,
    }




    private ElevatorMotorStates currentMotorState;

    public ElevatorStateMachine(){ currentMotorState = ElevatorMotorStates.IDLE; }


}
