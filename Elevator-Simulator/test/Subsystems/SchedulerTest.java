package Subsystems;

import Data.Direction;
import Data.Event;
import States.SchedulerState;
import org.junit.jupiter.api.Test;
import Data.Event;
import Data.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchedulerTest {

    @Test
    void testState(){
        Scheduler scheduler = new Scheduler();
        scheduler.setState(SchedulerState.schedulerStates.ReceiveRequest);
        assertEquals(SchedulerState.schedulerStates.ReceiveRequest, scheduler.getState());
    }

}