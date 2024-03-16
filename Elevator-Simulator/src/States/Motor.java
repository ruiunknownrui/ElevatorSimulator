package States;

import Data.Direction;
import Subsystems.Elevator;

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

    private Elevator elevator;
    private MotorStates currMotorState;
    private Direction moveDirection = Direction.Up;
    private static final double FLOOR_HEIGHT = 4D; // 4 meters per floor
    private static final double MAX_ACCEL = 0.13D; // m/s^2
    private static final double MAX_VELOCITY = 1.048D; // m/s
    private double acceleration = 0D;
    private double velocity = 0D;
    private double position = 0D;

    public Motor(Elevator elevator){
        this.elevator = elevator;
        this.currMotorState = MotorStates.IDLE;
    }

    // Updates the current motor state
    public void update(double deltaT){
        
    }
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
