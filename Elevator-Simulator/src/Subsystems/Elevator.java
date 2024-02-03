package Subsystems;

import Data.Event;

public class Elevator implements Runnable{

    private Scheduler schedulerSystem;
    private int currentFloor;

    public Elevator(Scheduler s){
        this.schedulerSystem = s;
        this.currentFloor = 1;
    }

    public void moveUp(){
        if(currentFloor < 8){
            currentFloor += 1;
            System.out.println("Elevator moves up to floor " + currentFloor);
        }
    }

    public void moveDown(){
        if(currentFloor > 1){
            currentFloor -= 1;
            System.out.println("Elevator moves down to floor " + currentFloor);
        }
    }

    public void run(){
        while (schedulerSystem.notFinish()){
            System.out.println("call nf from E");
            //TODO: Process event and make scheduler inactive.
            Event e = schedulerSystem.replyWork();
            System.out.println("Elevator received request from Scheduler " + e.getFloor() + " to go " + e.getFloorButton() +
                    " to floor " + e.getCarButton() + ".");
           try{
                Thread.sleep(400);
            } catch (InterruptedException ignored) {}
        }
    }
}
