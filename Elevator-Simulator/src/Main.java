import Subsystems.Elevator;
import Subsystems.Floor;
import Subsystems.RequestBuffer;
import Subsystems.Scheduler;

public class Main {
    public static void main(String[] args) {
        Thread floor, elevator, schedulerThread;

//        RequestBuffer requestBuffer;
//        requestBuffer = new RequestBuffer();

        Scheduler scheduler = new Scheduler();
        schedulerThread = new Thread(scheduler);
        floor = new Thread( new Floor());
//        elevator = new Thread( new Elevator(scheduler));

        schedulerThread.start();
        floor.start();
//        elevator.start();
    }
}