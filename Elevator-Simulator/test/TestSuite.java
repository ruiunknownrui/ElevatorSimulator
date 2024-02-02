import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({Data.EventTest.class, Subsystems.ElevatorTest.class, Subsystems.FloorTest.class, Subsystems.SchedulerTest.class})
public class TestSuite {
}
