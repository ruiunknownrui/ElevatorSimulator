import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({Data.EventTest.class, Subsystems.ElevatorTest.class, Subsystems.FloorTest.class,
        Subsystems.SchedulerTest.class, States.DoorTest.class, States.ElevatorButtonTest.class,
        States.MotorTest.class, States.SchedulerStateTest.class})
public class TestSuite {
}
