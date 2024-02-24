package States;

public class ElevatorButton {

    private enum ButtonStates{
        ON,
        OFF
    }

    private ButtonStates currButtonState;
    private int floorNumber;

    public ElevatorButton(int fN){
        this.floorNumber = fN;
        this.currButtonState = ButtonStates.OFF;
    }

    public void toggleButtonState(){
        switch (currButtonState){
            case ON -> currButtonState = ButtonStates.OFF;
            case OFF -> currButtonState = ButtonStates.ON;
        }
    }

    public int getFloorNumber(){ return this.floorNumber; }
}
