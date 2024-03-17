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
    private Thread schedulerReceive;
    private Thread schedulerSend;
    private Thread schedulerUpdate;

    /**
     * Scheduler creates the request buffer and three scheduler handlers which are used to send and receive message
     * it also initialize the elevator information HashMap and scheduler state.
     */
    public Scheduler(){
        this.requestBuffer = new RequestBuffer();
        this.state = SchedulerState.schedulerStates.WaitingState;
        System.out.println("Scheduler created with state: " + this.state);
        this.elevatorInfo = new HashMap<>();
        this.schedulerReceive =new Thread(new SchedulerReceiveHandler(this.requestBuffer, this)) ;
        this.schedulerSend = new Thread(new SchedulerSendHandler(this.requestBuffer, this));
        this.schedulerUpdate = new Thread(new SchedulerUpdateHandler(this));
    }

    /**
     * setState sets the new state to the scheduler
     * @param newState  new scheduler state
     */
    public void setState(SchedulerState.schedulerStates newState){
        this.state = newState;
        System.out.println("Scheduler State: " + this.state);
    }

    /**
     * getState returns the state of the scheduler  (only used in test)
     * @return  current state of the Scheduler
     */
    public SchedulerState.schedulerStates getState(){
        return this.state ;
    }

    /**
     * targetElevator determine the elevator which should receive the current request.
     * the elevator which located closest to the target floor will receive the request
     * @param event  current request
     * @return
     */
    public synchronized int targetElevator(Event event){
        //TODO: change the logic
        int startFloor = event.getFloor();
        int elevatorKey = -1;
        int closestFloor = -10;
        while (accessInfo || elevatorInfo.isEmpty()){
            try{
                wait();
            } catch (InterruptedException e){
                System.err.println(e);
            }
        }
        accessInfo = true;
        // keep finding the elevator which is closest to the start floor
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

    /**
     * updateElevatorInfo updates assign the new elevator information to the specific key
     * @param port
     * @param newInfo
     */
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
