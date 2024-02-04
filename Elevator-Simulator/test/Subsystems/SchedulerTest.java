package Subsystems;

import org.junit.jupiter.api.Test;
import Data.Event;
import Data.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchedulerTest {

    @Test
    void testAddAndGetEvent(){
        RequestBuffer newBuffer = new RequestBuffer();
        Scheduler scheduler = new Scheduler(newBuffer);
        Event aEvent = new Event("1", 1, Direction.Up, 3);
        scheduler.addEvent(aEvent);
        assertEquals(1, newBuffer.getEvents().size());
        scheduler.replyWork();
        assertEquals(0, newBuffer.getEvents().size());
    }

    @Test
    void testTotalRequest(){
        RequestBuffer newBuffer = new RequestBuffer();
        Scheduler scheduler = new Scheduler(newBuffer);
        assertEquals(0, scheduler.getTotalRequest());
        scheduler.setTotalRequest(5);
        assertEquals(5, scheduler.getTotalRequest());

    }
}