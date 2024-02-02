import Subsystems.Elevator;
import Subsystems.Floor;
import Subsystems.Scheduler;

public class Main {
    public static void main(String[] args) {
        Thread floor, elevator;

        Scheduler schedulerSubsystem;
        schedulerSubsystem = new Scheduler();

        floor = new Thread( new Floor(schedulerSubsystem));
        elevator = new Thread( new Elevator(schedulerSubsystem));

        floor.start();
        elevator.start();
    }
}