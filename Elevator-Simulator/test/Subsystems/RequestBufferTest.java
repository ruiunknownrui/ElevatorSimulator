package Subsystems;

import Data.Event;
import Data.Direction;
import Data.FaultConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RequestBufferTest {
    private RequestBuffer requestBuffer;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        requestBuffer = new RequestBuffer();
        testEvent = new Event("14:05:15.0", 2, Direction.Up, 3, FaultConstant.Fault.NONE);
    }

    @Test
    void testAddToEvents() {
        requestBuffer.addToEvents(testEvent);
        assertFalse(requestBuffer.getEvents().isEmpty(), "Events list should not be empty after adding an event.");
    }

    @Test
    void testGetNextEvent() {
        requestBuffer.addToEvents(testEvent);
        Event event = requestBuffer.getNextEvent();
        assertEquals(testEvent, event, "The retrieved event should match the one that was added.");
    }

    @Test
    void testBufferEmptyAfterGetNextEvent() {
        requestBuffer.addToEvents(testEvent);
        requestBuffer.getNextEvent();
        assertTrue(requestBuffer.getEvents().isEmpty(), "Events list should be empty after retrieving the event.");
    }


}
