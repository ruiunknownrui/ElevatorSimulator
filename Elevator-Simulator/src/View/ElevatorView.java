package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * ElevatorView generate GUI components for each elevator. It is a scrollable read only text area
 * and a list of JPanels which represent elevator at each floor.
 */
public class ElevatorView extends JPanel {

    private final int NUM_OF_FLOOR = 22;
    private final Color FAULT = Color.red;
    private final Color CURRENT = Color.GREEN;
    private final Color NORMAL = Color.white;
    private JTextArea description;
//    private JPanel floorOverview;
    private ArrayList<JPanel> floors;
    private JLabel nameLabel;

    /**
     * ElevatorView creates the test area and list panels. It also sets the current JPanel in BorderLayout, and
     * creates the JLabel that displays elevator name.
     */
    public ElevatorView(){
        this.floors = new ArrayList<JPanel>();
        this.nameLabel = new JLabel();

        this.setLayout(new BorderLayout());

        // Creates and adds the description area to scrollPanel and show the scrollbar as needed.
        this.description = new JTextArea();
        this.description.setEditable(false);
        JScrollPane scrollText = new JScrollPane(this.description);
        scrollText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Creates the JPanel which contains 22 JPanels that represents the elevator at each floor.
        JPanel elevators = new JPanel();
        elevators.setLayout(new GridLayout(NUM_OF_FLOOR, 1));
        for (int i = 1; i <= NUM_OF_FLOOR; i++){
            JPanel floorPanel = new JPanel(new GridLayout(1, 1));
            floorPanel.setSize(100, 20);
            floorPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            floorPanel.setBackground(NORMAL);
            JLabel floorLabel = new JLabel(String.valueOf(i));
            floorPanel.add(floorLabel);
            this.floors.add(floorPanel);
            elevators.add(floorPanel);
        }

        // Add name, floor view and description part to the panel
        this.add(this.nameLabel, BorderLayout.NORTH);
        this.add(elevators, BorderLayout.WEST);
        this.add(this.description, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }

    /**
     * setElevatorName sets the elevator name to the label
     * @param name
     */
    public void setElevatorName(String name){
        this.nameLabel.setText(name);
    }

    /**
     * updates the color of current floor and previous floor to display elevator movement
     * @param previousFloor  previous floor number
     * @param curFloor   current floor number
     */
    public void updateFloor(int previousFloor, int curFloor){
        if (previousFloor > 0){  // If previous floor exist
            // Get the JPanel that represents the previous floor and set to white
            JPanel floorPanel = this.floors.get(previousFloor - 1);
            floorPanel.setBackground(NORMAL);
        }
        if(curFloor > 0){  // Double-Check the floor is valid
            // Get the JPanel that represents the current floor and set the green
            JPanel floorPanel = this.floors.get(curFloor - 1);
            floorPanel.setBackground(CURRENT);
        }
    }

    /**
     * displayFault updates the floor color that represent fault or normal, depends on if it is solved or not
     * @param curFloor  the floor number of the fault floor
     * @param isSolved  if the fault is solved
     */
    public void displayFault(int curFloor, boolean isSolved){
        if(curFloor > 0){  // Double check if the floor number is valid
            JPanel floorPanel = this.floors.get(curFloor - 1);
            if (isSolved) {
                floorPanel.setBackground(NORMAL);
            }else {
                floorPanel.setBackground(FAULT);
            }
        }
    }

    /**
     * updateDescription adds new description to the text displayed area
     * @param newDescription
     */
    public void updateDescription(String newDescription){
        this.description.append(newDescription);
    }
}
