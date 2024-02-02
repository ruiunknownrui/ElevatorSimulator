package Subsystems;

import Data.Direction;
import Data.Event;
import States.SchedulerState;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Floor implements Runnable{

    private Scheduler scheduler;

    public Floor(Scheduler s){this.scheduler = s;}

    private ArrayList<Event> readInput() {
        File events = new File("../src/input.txt");
        ArrayList<Event> eventsData = new ArrayList<>();

        try {
            Scanner s = new Scanner(events);
            while (s.hasNext()) {
                String event = s.next();
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

        while(scheduler.getState() == SchedulerState.Active){

        }
    }
}
