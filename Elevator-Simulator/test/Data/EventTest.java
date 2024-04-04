package Data;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    @org.junit.jupiter.api.Test
    void getTime() {
    }

    @org.junit.jupiter.api.Test
    void getFloor() {
    }

    @org.junit.jupiter.api.Test
    void getFloorButton() {
    }

    @org.junit.jupiter.api.Test
    void getCarButton() {
    }

    @Test
    void serialize() throws IOException, ClassNotFoundException {
        Event testEvent = new Event("14:05:15.0", 2 ,Direction.Up, 4, FaultConstant.Fault.NONE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(testEvent);
        byte[] data = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Event deserializedEvent = (Event)ois.readObject();
        assertEquals(testEvent.getTime(), deserializedEvent.getTime());
        assertEquals(testEvent.getFloor(), deserializedEvent.getFloor());
        assertEquals(testEvent.getCarButton(), deserializedEvent.getCarButton());
        assertEquals(testEvent.getFloorButton(), deserializedEvent.getFloorButton());
    }
}