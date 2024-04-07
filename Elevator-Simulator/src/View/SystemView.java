package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SystemView extends JFrame {

    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;

    // Display the window in the center of the screen
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int CENTER_X = Math.round(((int)SCREEN_SIZE.getWidth()) - FRAME_WIDTH) / 2;
    private static final int CENTER_Y = Math.round(((int)SCREEN_SIZE.getHeight()) - FRAME_HEIGHT) / 2;

    private ArrayList<ElevatorView> elevatorViews;  // List of all elevator view

    public SystemView(){
        this.elevatorViews = new ArrayList<ElevatorView>();

        // Set window size and location
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocation(CENTER_X, CENTER_Y);

        this.setLayout(new GridLayout(1, 4));

        ElevatorView eView1 = new ElevatorView();
        this.add(eView1);
        this.elevatorViews.add(eView1);

        ElevatorView eView2 = new ElevatorView();
        this.add(eView2);
        this.elevatorViews.add(eView2);

        ElevatorView eView3 = new ElevatorView();
        this.add(eView3);
        this.elevatorViews.add(eView3);

        ElevatorView eView4 = new ElevatorView();
        this.add(eView4);
        this.elevatorViews.add(eView4);

        this.setVisible(true);
    }

    /**
     * getElevatorView gets and returns specific elevator views
     * @param i the index of elevator view
     * @return the specific elevator view
     */
    public ElevatorView getElevatorView(int i){
        return this.elevatorViews.get(i);
    }
}
