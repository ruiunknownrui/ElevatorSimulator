package Subsystems;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ElevatorTest {
    private Elevator elevator;
    private Scheduler scheduler; // Assuming Scheduler is needed based on the Elevator class.

    @BeforeEach
    void setUp() {
        scheduler = new Scheduler(new RequestBuffer()); // Modify as needed based on actual constructors.
        elevator = new Elevator(scheduler);
    }

    @Test
    void testInitialFloor() {
        // Assuming there's a method in Elevator to get the current floor.
        assertEquals(1, elevator.getCurrentFloor(), "Elevator should start at ground floor.");
    }

    @Test
    void testMoveUp() {
        elevator.moveUp();
        assertEquals(2, elevator.getCurrentFloor(), "Elevator should move up by one floor.");
    }

    @Test
    void testMoveDown() {
        // Moving the elevator up first, then down, to avoid going below ground floor.
        elevator.moveUp();
        elevator.moveDown();
        assertEquals(1, elevator.getCurrentFloor(), "Elevator should move down to ground floor.");
    }
    
}
