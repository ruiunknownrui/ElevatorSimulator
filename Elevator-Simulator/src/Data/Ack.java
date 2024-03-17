package Data;

import java.io.Serializable;

/**
 * Ack class store the string of Acknowledge information.
 * @author Rebecca Li
 */
public class Ack implements Serializable{
    private String ack;

    public Ack(String ack){
        this.ack = "Ack: " + ack;
    }

    public String getAck() {
        return ack;
    }
}
