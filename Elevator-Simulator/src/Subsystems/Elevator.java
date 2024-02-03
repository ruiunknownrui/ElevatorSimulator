package Subsystems;

import Data.Event;

public class Elevator implements Runnable{

    private RequestBuffer requestBuffer;
    private int currentFloor;

    public Elevator(RequestBuffer s){
        this.requestBuffer = s;
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
        while (requestBuffer.notFinish()){
            //TODO: Process event and make scheduler inactive.
            Event e= requestBuffer.getNextEvent();
            System.out.println("Elevator received request from Scheduler " + e.getFloor() + " to go " + e.getFloorButton() +
                    " to floor " + e.getCarButton() + ".");
           try{
                Thread.sleep(400);
            } catch (InterruptedException ignored) {}
        }
    }
}
