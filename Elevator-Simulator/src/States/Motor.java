package States;

/**
 * Class for the state machine of the Motor subcomponent of the Elevator.
 */
public class Motor {

    /**
     * States for Motor are: IDLE, ACCELERATING, MOVING_AT_MAX_VELOCITY, DECELERATING.
     */
    public enum MotorStates {
        IDLE,
        ACCELERATING,
        MOVING_AT_MAX_VELOCITY,
        DECELERATING
    }

    private MotorStates currMotorState;
    private int movingTime;

    public Motor(){ this.currMotorState = MotorStates.IDLE;}

    /**
     * Changes the state of the motor based on the movement of the Elevator.
     */
    public void moveElevator(){
        switch (currMotorState){
            case IDLE:
                currMotorState = MotorStates.ACCELERATING;
                System.out.println("Elevator Motor State: Accelerating.");
                break;
            case ACCELERATING:
                currMotorState = MotorStates.MOVING_AT_MAX_VELOCITY;
                System.out.println("Elevator Motor State: Moving at Max Velocity.");
                break;
            case MOVING_AT_MAX_VELOCITY:
                currMotorState = MotorStates.DECELERATING;
                System.out.println("Elevator Motor State: Decelerating.");
                break;
            case DECELERATING:
                currMotorState = MotorStates.IDLE;
                System.out.println("Elevator Motor State: Idle.");
                break;
        }
    }

    /**
     * Gets the current state of the motor.
     * @return current Motor state.
     */
    public MotorStates getCurrMotorState() { return currMotorState; }
}
