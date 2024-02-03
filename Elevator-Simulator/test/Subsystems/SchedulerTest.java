package Subsystems;

import Data.Direction;
import Data.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {

    @Test
    void hasWork() {
    }

    @Test
    void addToEvents() {
    }

    @Test
    void getNextEvent() {
    }

    @Test
    void addEvent(){
        RequestBuffer newBuffer = new RequestBuffer();
        Scheduler scheduler = new Scheduler(newBuffer);
        Event aEvent = new Event("1", 1, Direction.Up, 3);
        scheduler.addEvent(aEvent);
        assertEquals(1, scheduler.getEvents().size());
    }
}