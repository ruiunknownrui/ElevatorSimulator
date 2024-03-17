package States;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SchedulerStateTest class is the test class of SchedulerState class
 */
public class SchedulerStateTest {

    /**
     * testUpdateState tests the updateState function
     */
    @Test
    void testUpdateState(){
        assertEquals(SchedulerState.schedulerStates.RequestSent, SchedulerState.updateState(false, true));
        assertEquals(SchedulerState.schedulerStates.ReceiveRequest, SchedulerState.updateState(true, false));
        assertEquals(SchedulerState.schedulerStates.WaitingState, SchedulerState.updateState(false, false));
    }
}
