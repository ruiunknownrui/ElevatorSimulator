package Data;

import java.io.Serializable;

/**
 * Ack class store the string of Acknowledge information.
 * @author Rebecca Li
 */
public class Ack implements Serializable{
    private String ack;

    /**
     * Initialize the ack string of the object
     * @param ack
     */
    public Ack(String ack){
        this.ack = "Ack: " + ack;
    }

    /**
     * getAck returns the ack string of the object
     * @return
     */
    public String getAck() {
        return ack;
    }
}
