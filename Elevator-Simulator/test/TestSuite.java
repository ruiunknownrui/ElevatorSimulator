import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
//Subsystems.ElevatorTest.class,
@Suite
@SelectClasses({Data.AckTest.class, Data.ElevatorInfoTest.class, Data.EventTest.class,  Subsystems.FloorTest.class, Subsystems.ElevatorTest.class,
        Subsystems.SchedulerTest.class, States.DoorTest.class, States.ElevatorButtonTest.class,
        States.MotorTest.class, States.SchedulerStateTest.class})
public class TestSuite {
}
