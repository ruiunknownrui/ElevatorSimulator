package States;

import View.ElevatorView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Door, exhausts all states of the state machine and asserts current state with expected value.
 */
public class DoorTest {

    private Door testDoors = new Door(new ElevatorView());

    @Test
    void operateDoors() {
        assertEquals(Door.DoorStates.DOORS_CLOSED, testDoors.getCurrDoorState());
        testDoors.operateDoors();
        assertEquals(Door.DoorStates.DOORS_OPENING, testDoors.getCurrDoorState());
        testDoors.operateDoors();
        assertEquals(Door.DoorStates.DOORS_OPEN, testDoors.getCurrDoorState());
        testDoors.operateDoors();
        assertEquals(Door.DoorStates.DOORS_CLOSING, testDoors.getCurrDoorState());
        testDoors.operateDoors();
        assertEquals(Door.DoorStates.DOORS_CLOSED, testDoors.getCurrDoorState());
    }
}