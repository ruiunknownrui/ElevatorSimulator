package States;

import Data.Direction;
import Subsystems.Elevator;

/**
 * Class for the state machine of the Motor subcomponent of the Elevator.
 */
public class Motor {

    /**
     * States for Motor are: IDLE, MOVING
     */
    public enum MotorStates {
        IDLE,
        MOVING
    }

    private Elevator elevator;
    private MotorStates currMotorState;
    private final int movingTime = 10000;  // 10 seconds -> 10000 milliseconds
    
    public Motor(Elevator elevator){
        this.elevator = elevator;
        this.currMotorState = MotorStates.IDLE;
    }

    /**
     * updateMotorState updates the current motor state
     */
    public void updateMotorState(){
        if (this.currMotorState == MotorStates.IDLE){
            this.currMotorState = MotorStates.MOVING;
        }else {
            this.currMotorState = MotorStates.IDLE;
        }
    }

    /**
     * elevatorMoving assume the elevator is moving for 10 seconds
     */
    public void elevatorMoving(){
        try{
            Thread.sleep(this.movingTime);
        }catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    /**
     * Gets the current state of the motor.
     * @return current Motor state.
     */
    public MotorStates getCurrMotorState() { return currMotorState; }
}
