package Subsystems;

import Data.Direction;
import Data.Event;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class name: Floor
 * Purpose: Represents the Floor subsystem, sends events/requests to the Scheduler subsystem.
 */
public class Floor implements Runnable{

    private Scheduler schedulerSystem;

    public Floor(Scheduler schedulerSystem){
        this.schedulerSystem = schedulerSystem;
    }

    /**
     * Name: readInput()
     * Purpose: Opens the input file and reads it line by line, creating an Event object for each line of the file
     *          then returns the final list of all the Event objects.
     * In: Input file.
     * @return List of events read from input file.
     */
    public ArrayList<Event> readInput() {
        File events = new File("src/input.txt");
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

    /**
     * Name: run()
     * Purpose: Run function for Floor thread. Loops through list of events read from input file and passes
     *          each one to the scheduler thread.
     * In: None
     * Out: None
     */
    public void run(){
        ArrayList<Event> eventsInput = readInput();
        int totalRequest = 0;
        for (Event event : eventsInput) {
            totalRequest += 1;
            System.out.println("Floor sends request to Scheduler " + event.getFloor() +
                    " to go " + event.getFloorButton() + " to floor " + event.getCarButton() + ".");
            schedulerSystem.addEvent(event);  // Sends request to scheduler system
            try{
                Thread.sleep(300);
            } catch (InterruptedException e) { return; }
        }
        schedulerSystem.setTotalRequest(totalRequest);
        Scheduler.floorDone();
        System.out.println("floor done");
    }
}
