package States;

import Subsystems.Elevator;
import Subsystems.RequestBuffer;
import Subsystems.Scheduler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Motor, exhausts all states for the state machine and asserts with expected value.
 */
public class MotorTest {
    private RequestBuffer testBuffer = new RequestBuffer();
    private Scheduler testScheduler = new Scheduler();
    private Elevator testElevator = new Elevator(4000);
    private Motor testMotor = new Motor(testElevator);

    @Test
    void moveElevator() {
        assertEquals(Motor.MotorStates.IDLE, testMotor.getCurrMotorState());
        testMotor.moveElevator();
        assertEquals(Motor.MotorStates.ACCELERATING, testMotor.getCurrMotorState());
        testMotor.moveElevator();
        assertEquals(Motor.MotorStates.MOVING_AT_MAX_VELOCITY, testMotor.getCurrMotorState());
        testMotor.moveElevator();
        assertEquals(Motor.MotorStates.DECELERATING, testMotor.getCurrMotorState());
        testMotor.moveElevator();
        assertEquals(Motor.MotorStates.IDLE, testMotor.getCurrMotorState());
    }
}