import Subsystems.Elevator;
import Subsystems.Floor;
import Subsystems.RequestBuffer;

public class Main {
    public static void main(String[] args) {
        Thread floor, elevator;

        RequestBuffer requestBufferSubsystem;
        requestBufferSubsystem = new RequestBuffer();

        floor = new Thread( new Floor(requestBufferSubsystem));
        elevator = new Thread( new Elevator(requestBufferSubsystem));

        floor.start();
        elevator.start();
    }
}