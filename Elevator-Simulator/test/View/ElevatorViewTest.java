package View;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorViewTest {

    @Test
    void setElevatorName() {
        ElevatorView elevator = new ElevatorView();
        elevator.setElevatorName("Test Elevator");

        assertEquals("Test Elevator", elevator.getElevatorName());
    }

    @Test
    void updateFloor() {
        ElevatorView elevator = new ElevatorView();
        elevator.updateFloor(2, 3);

        assertEquals(Color.white, elevator.getFloorPanelColor(2));
        assertEquals(Color.GREEN, elevator.getFloorPanelColor(3));
    }

    @Test
    void displayFault() {
        ElevatorView elevator = new ElevatorView();
        elevator.displayFault(2,true);
        assertEquals(Color.GREEN, elevator.getFloorPanelColor(2));

        elevator.displayFault(2,false);
        assertEquals(Color.red, elevator.getFloorPanelColor(2));
    }

    @Test
    void updateDescription() {
        ElevatorView elevator = new ElevatorView();
        elevator.updateDescription("Test Description");

        assertTrue(elevator.getDescription().contains("Test Description"));
    }
}