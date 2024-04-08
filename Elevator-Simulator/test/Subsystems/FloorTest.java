package Subsystems;

import Data.Direction;
import Data.Event;
import Data.FaultConstant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FloorTest {

    /**
     * Test Function
     * Name: testReadInput()
     * Purpose: tests the readInput() function of the Floor class by creating an ArrayList of Event objects
     *          and comparing it to the list returned by the function after reading the file.
     * In: None
     * Out:
     */
    @Test
    void testReadInput() {

        Floor testFloor = new Floor();
        ArrayList<Event> realList = testFloor.readInput();

        ArrayList<Event> testList = new ArrayList<>();
        Event event1 = new Event("14:05:15.0", 2, Direction.Up, 4, FaultConstant.Fault.NONE);
        testList.add(event1);
        Event event2 = new Event("14:35:12.0", 1, Direction.Up, 3, FaultConstant.Fault.NONE);
        testList.add(event2);
        Event event3 = new Event("14:46:10.0", 7, Direction.Down, 6, FaultConstant.Fault.DOOR_STUCK_OPEN);
        testList.add(event3);
        Event event4 = new Event("18:25:39.0", 4, Direction.Down, 2, FaultConstant.Fault.ELEVATOR_STUCK);
        testList.add(event4);
        Event event5 = new Event("19:00:00.0", 1, Direction.Up, 22, FaultConstant.Fault.NONE);
        testList.add(event5);

        for(int i = 0; i < testList.size(); i++){
            assertEquals(testList.get(i).getTime(), realList.get(i).getTime());
            assertEquals(testList.get(i).getFloor(), realList.get(i).getFloor());
            assertEquals(testList.get(i).getFloorButton(), realList.get(i).getFloorButton());
            assertEquals(testList.get(i).getCarButton(), realList.get(i).getCarButton());
        }
    }
}