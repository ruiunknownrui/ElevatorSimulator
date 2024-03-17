package Data;

import java.io.Serializable;

public class Event implements Serializable {

    private String time;
    private int floor;
    private Direction floorButton;
    private int carButton;

    public Event(String t, int f, Direction fB, int cB){
        this.time = t;
        this.floor = f;
        this.floorButton = fB;
        this.carButton = cB;
    }

    public String getTime() {
        return time;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getFloorButton() {
        return floorButton;
    }

    public int getCarButton() {
        return carButton;
    }

    /**
     * toString returns the string format information of the object
     * @return  the information of the object
     */
    public String toString(){
        return "Time: " + time + ", Floor: " + floor + ", Floor Button: " + floorButton + ", Car Button: " + carButton;
    }
}

