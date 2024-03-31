package Data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElevatorInfoTest {
    @Test
    void testElevatorInfo(){
        ElevatorInfo eI = new ElevatorInfo(1, true, false);
        assertEquals(1, eI.getCurrFloor());
        assertEquals(true, eI.isDoingRequest());
        assertEquals(false, eI.isShutDown());
    }
}
