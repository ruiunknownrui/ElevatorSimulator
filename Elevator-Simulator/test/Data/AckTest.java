package Data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AckTest {
    @Test
    void testAck(){
        Ack ack = new Ack("test ack");
        assertEquals("Ack: test ack", ack.getAck());
    }
}
