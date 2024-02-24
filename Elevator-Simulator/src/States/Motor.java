package States;

public class Motor {

    private enum MotorStates {
        IDLE,
        ACCELERATING,
        MOVING_AT_MAX_VELOCITY,
        DECELERATING
    }

    private MotorStates currMotorState;

    public Motor(){ this.currMotorState = MotorStates.IDLE; }

    public void moveElevator(){
        switch (currMotorState){
            case IDLE -> currMotorState = MotorStates.ACCELERATING;
            case ACCELERATING -> currMotorState = MotorStates.MOVING_AT_MAX_VELOCITY;
            case MOVING_AT_MAX_VELOCITY -> currMotorState = MotorStates.DECELERATING;
            case DECELERATING -> currMotorState = MotorStates.IDLE;
        }
    }
}
