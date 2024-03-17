package Data;

/**
 * Ack class store the string of Acknowledge information.
 * @author Rebecca Li
 */
public class Ack {
    private String ack;

    public Ack(String ack){
        this.ack = "Ack: " + ack;
    }

    public String getAck() {
        return ack;
    }
}
