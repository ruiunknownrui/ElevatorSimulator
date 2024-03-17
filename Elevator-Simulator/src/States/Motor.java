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
    private static final int MIN_FLOOR = 1;
    private static final int MAX_FLOOR = 8;
    private double acceleration = 0D;
    private double velocity = 0D;
    private double position = 0D;
    private int currFloor = MIN_FLOOR;

    public Motor(Elevator elevator){
        this.elevator = elevator;
        this.currMotorState = MotorStates.IDLE;
    }

    /**
     * Updates elevator motors acceleration, velocity, and position based on currMotorState,
     * and changes to new states accordingly
     * @param deltaT seconds since last update, used to calculate kinematics
     */
    public void update(double deltaT){
        switch (currMotorState){
            case ACCELERATING -> {
                acceleration = MAX_ACCEL;
                updateVelocity(acceleration * ((moveDirection == Direction.Up)? 1 : -1), deltaT);
                if (Math.abs(velocity) == MAX_VELOCITY){
                    currMotorState = MotorStates.MOVING_AT_MAX_VELOCITY;
                }
                updatePosition(velocity, deltaT);
            }
            case DECELERATING -> {
                acceleration = -MAX_ACCEL;
                updateVelocity(acceleration * ((moveDirection == Direction.Up)? 1 : -1), deltaT);
                if (velocity == 0) {
                    // Finished slowing down
                    currMotorState = MotorStates.IDLE;
                }
                updatePosition(velocity, deltaT);
            }
            case MOVING_AT_MAX_VELOCITY -> {
                updatePosition(velocity, deltaT);
            }
            case IDLE -> {
                acceleration = 0;
                velocity = 0;
            }
        }
    }

    /**
     * Updates elevators velocity based on given acceleration value and deltaT.
     * If the given acceleration is slowing down relative to the current velocity and movement direction, it will automatically
     * clamp the velocity to 0m/s to ensure the elevator doesn't start moving backwards.
     * @param accel elevator acceleration in m/s. Positive is accelerating/decelerating up, negative is accel/decel down
     * @param deltaT seconds elapsed since last update
     */
    private void updateVelocity(double accel, double deltaT){
        velocity += accel * deltaT; // Accel negative if moving down, positive otherwise
        // Make sure velocity isn't negative if elevator is moving up, and vice versa
        // This is to ensure the elevator velocity snaps to 0 when decelerating
        if ((moveDirection == Direction.Up && accel < 0 && velocity < 0) || (moveDirection == Direction.Down && accel > 0 && velocity > 0)){
            velocity = 0;
        }
        if (Math.abs(velocity) >= MAX_VELOCITY) {
            // set to max velocity, with proper sign
            velocity = MAX_VELOCITY * ((velocity < 0)? -1 : 1);
        }
    }

    /**
     * Updates elevators position based on given velocity value and deltaT.
     * If the given velocity is 0, will snap position to nearest floor.
     * When the elevator reaches a new floor, will call newFloorReached() event.
     * @param vel elevator velocity in m/s. Positive is moving up, negative is moving down
     * @param deltaT seconds elapsed since last update
     */
    private void updatePosition(double vel, double deltaT) {
        position += vel * deltaT;
        // check if new floor threshold reached
        double currFloorPosition = (currFloor - MIN_FLOOR) * FLOOR_HEIGHT;
        if (vel < 0){
            // If moving down, don't change floor number until 4 meters below current floor's position
            if (position <= currFloorPosition - FLOOR_HEIGHT){
                newFloorReached();
            }
        } else if (vel > 0) {
            // If moving up, don't change floor number until 4 meters above current floor's position
            if (position >= currFloorPosition + FLOOR_HEIGHT){
                newFloorReached();
            }
        } else {
            // Velocity is 0, snap to nearest floor position
            int nearestFloor = MIN_FLOOR + (int)Math.round(position / FLOOR_HEIGHT);
            if (nearestFloor != currFloor){
                newFloorReached();
                position = (currFloor - MIN_FLOOR) * FLOOR_HEIGHT;
            }
        }
    }

    /**
     * Event that gets called when the elevator has reached a new floor while travelling.
     * Updates currFloor up or down depending on current movement direction,
     * then sends appropriate signal to elevator (moveUp or moveDown)
     */
    public void newFloorReached(){
        int newFloorNum;
        if (moveDirection == Direction.Up){
            // move floor up
            newFloorNum = currFloor + 1;
            assert(newFloorNum <= MAX_FLOOR);
            elevator.moveUp();
        } else {
            // move floor down
            newFloorNum = currFloor - 1;
            assert(newFloorNum >= MIN_FLOOR);
            elevator.moveDown();
        }
        currFloor = newFloorNum;
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
