package messages;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;


public abstract class MapMessage implements Serializable {
    String key;
    String value;

    public MapMessage(String key) {
        this.key = key;
    }

    public MapMessage(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public abstract void action(Map<String, String> state);
}
