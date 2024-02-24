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
        SchedulerState state = new SchedulerState();
        assertEquals(SchedulerState.schedulerStates.NoRequest, state.getCurrState());
        state.updateState();
        assertEquals(SchedulerState.schedulerStates.HasRequest, state.getCurrState());
        state.updateState();
        assertEquals(SchedulerState.schedulerStates.RequestSent, state.getCurrState());
        state.updateState();
        assertEquals(SchedulerState.schedulerStates.RequestFinish, state.getCurrState());
    }
}
