package States;

/**
 * Class for the state machine of the Elevator Button subcomponent.
 */
public class ElevatorButton {

    /**
     * States of the button are ON, or OFF.
     */
    public enum ButtonStates{
        ON,
        OFF
    }

    private ButtonStates currButtonState;
    private int floorNumber;

    public ElevatorButton(int fN){
        this.floorNumber = fN;
        this.currButtonState = ButtonStates.OFF;
    }

    /**
     * Toggles the state of the button from ON to OFF or vice versa to simulate the button being pressed
     * or the Elevator reaching the pressed floor.
     */
    public void toggleButtonState(){
        switch (currButtonState){
            case ON -> currButtonState = ButtonStates.OFF;
            case OFF -> currButtonState = ButtonStates.ON;
        }
    }

    /**
     * Get the floor number this button corresponds to.
     * @return floor number
     */
    public int getFloorNumber(){ return this.floorNumber; }

    /**
     * Get the current state of the button.
     * @return current Button state.
     */
    public ButtonStates getCurrButtonState() { return currButtonState; }
}
