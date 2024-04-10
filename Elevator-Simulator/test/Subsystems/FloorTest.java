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
        Event event1 = new Event("02:22:00:15", 2, Direction.Up, 18, FaultConstant.Fault.NONE);
        testList.add(event1);
        Event event2 = new Event("02:22:12:45", 1, Direction.Up, 19, FaultConstant.Fault.NONE);
        testList.add(event2);
        Event event3 = new Event("02:22:25:30", 1, Direction.Up, 20, FaultConstant.Fault.DOOR_STUCK_OPEN);
        testList.add(event3);
        Event event4 = new Event("02:22:38:00", 2, Direction.Down, 1, FaultConstant.Fault.ELEVATOR_STUCK);
        testList.add(event4);
        Event event5 = new Event("02:22:50:45", 3, Direction.Up, 7, FaultConstant.Fault.NONE);
        testList.add(event5);

        for(int i = 0; i < testList.size(); i++){
            assertEquals(testList.get(i).getTime(), realList.get(i).getTime());
            assertEquals(testList.get(i).getFloor(), realList.get(i).getFloor());
            assertEquals(testList.get(i).getFloorButton(), realList.get(i).getFloorButton());
            assertEquals(testList.get(i).getCarButton(), realList.get(i).getCarButton());
        }
    }
}