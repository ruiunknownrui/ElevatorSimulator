package States;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ElevatorButton, exhausts all states for state machine and asserts with expected values
 */
public class ElevatorButtonTest {

    ElevatorButton testButton = new ElevatorButton(1);
    @Test
    void toggleButtonState() {
        assertEquals(ElevatorButton.ButtonStates.OFF, testButton.getCurrButtonState());
        testButton.toggleButtonState();
        assertEquals(ElevatorButton.ButtonStates.ON, testButton.getCurrButtonState());
    }
}