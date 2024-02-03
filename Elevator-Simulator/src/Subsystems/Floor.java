package Subsystems;

import Data.Direction;
import Data.Event;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Floor implements Runnable{

    private Scheduler schedulerSystem;

    public Floor(Scheduler schedulerSystem){
        this.schedulerSystem = schedulerSystem;
    }

    private ArrayList<Event> readInput() {
        File events = new File("Elevator-Simulator/src/input.txt");
        ArrayList<Event> eventsData = new ArrayList<>();

        try {
            Scanner s = new Scanner(events);
            while (s.hasNextLine()) {
                String event = s.nextLine();
                System.out.println("Read the following event from the file: " + event);

                String[] eventData = event.split(" ");
                Direction d;
                if(eventData[2].equalsIgnoreCase("up")){
                    d = Direction.Up;
                } else if (eventData[2].equalsIgnoreCase("down")){
                    d = Direction.Down;
                } else {
                    return null;
                }

                Event e = new Event(eventData[0], Integer.parseInt(eventData[1]), d, Integer.parseInt(eventData[3]));
                eventsData.add(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return eventsData;
    }
    public void run(){
        ArrayList<Event> eventsInput = readInput();
        for (Event event : eventsInput) {
            schedulerSystem.addEvent(event);  // Sends request to scheduler system
            System.out.println("Floor sends request to Scheduler " + event.getFloor() +
                    " to go " + event.getFloorButton() + " to floor " + event.getCarButton() + ".");
            try{
                Thread.sleep(300);
            } catch (InterruptedException e) { return; }
        }
        Scheduler.floorDone();
    }
}
