package States;

import Subsystems.Elevator;
import Subsystems.RequestBuffer;
import Subsystems.Scheduler;
import View.ElevatorView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Motor, exhausts all states for the state machine and asserts with expected value.
 */
public class MotorTest {
    private RequestBuffer testBuffer = new RequestBuffer();
    private Scheduler testScheduler = new Scheduler();
    private Elevator testElevator = new Elevator(4000, new ElevatorView());
    private Motor testMotor = new Motor(testElevator);

    @Test
    void moveElevator() {
        assertEquals(Motor.MotorStates.IDLE, testMotor.getCurrMotorState());
        testMotor.elevatorMoving();
        assertEquals(Motor.MotorStates.MOVING, testMotor.getCurrMotorState());
        testMotor.elevatorMoving();
        assertEquals(Motor.MotorStates.IDLE, testMotor.getCurrMotorState());
//        testMotor.moveElevator();
//        assertEquals(Motor.MotorStates.DECELERATING, testMotor.getCurrMotorState());
//        testMotor.moveElevator();
//        assertEquals(Motor.MotorStates.IDLE, testMotor.getCurrMotorState());
    }
}