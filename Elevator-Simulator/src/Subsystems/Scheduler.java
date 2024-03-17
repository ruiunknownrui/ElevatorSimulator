package Subsystems;

import Data.ElevatorInfo;
import Data.Event;
import States.SchedulerState;

import java.util.HashMap;


/**
 * Scheduler class keep adding requests from floor to the buffer. It creates RequestBuffer, SchedulerReceiveHandler,
 * SchedulerSendHandler, and SchedulerUpdateHandler.
 * Scheduler class store the state of scheduler, and elevator information. It also contains the function that
 * determines the target elevator.
 * @author Rebecca Li
 */
public class Scheduler implements Runnable{

    private RequestBuffer requestBuffer;
    private SchedulerState.schedulerStates state;
    private HashMap<Integer, ElevatorInfo> elevatorInfo;
    private boolean accessInfo;  // use for critical section
//    private SchedulerReceiveHandler schedulerReceive;
//    private SchedulerSendHandler schedulerSend;
//    private SchedulerUpdateHandler schedulerUpdate;
    private Thread schedulerReceive;
    private Thread schedulerSend;
    private Thread schedulerUpdate;
    /**
     * Scheduler creates the request buffer and three scheduler handlers which are used to send and receive message
     */
    public Scheduler(){
        this.requestBuffer = new RequestBuffer();
        this.state = SchedulerState.schedulerStates.WaitingState;
        System.out.println("Scheduler created with state: " + this.state);
        this.schedulerReceive =new Thread(new SchedulerReceiveHandler(this.requestBuffer, this)) ;
        this.schedulerSend = new Thread(new SchedulerSendHandler(this.requestBuffer, this));
        this.schedulerUpdate = new Thread(new SchedulerUpdateHandler(this.requestBuffer, this));
    }

    public void setState(SchedulerState.schedulerStates newState){
        this.state = newState;
        System.out.println("Scheduler State: " + this.state);
    }

    public synchronized int targetElevator(Event event){
        int startFloor = event.getFloor();
        int elevatorKey = -1;
        int closestFloor = -10;
        while (accessInfo || elevatorInfo.size() == 0){
            try{
                wait();
            } catch (InterruptedException e){
                System.err.println(e);
            }
        }
        accessInfo = true;
        for (var elevator : elevatorInfo.entrySet()){
            if (closestFloor == -10 ||
                    (Math.abs(startFloor - closestFloor) >
                            Math.abs(startFloor - elevator.getValue().getCurrFloor()))){
                elevatorKey = elevator.getKey();
                closestFloor = elevator.getValue().getCurrFloor();
                }
            }
        notifyAll();
        accessInfo = false;
        return elevatorKey;
    }

    public synchronized void updateElevatorInfo(int port, ElevatorInfo newInfo){
        while (accessInfo){
            try{
                wait();
            } catch (InterruptedException e){
                System.err.println(e);
            }
        }
        accessInfo = true;
        elevatorInfo.put(port, newInfo);
        accessInfo = false;
        notifyAll();
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        this.schedulerReceive.start();
        this.schedulerSend.start();
        this.schedulerUpdate.start();
    }

    public static void main(String[] args) {
        Thread scheduler;

        scheduler = new Thread( new Scheduler());

        scheduler.start();
    }
}
