package Data;

public class Event {

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
}

