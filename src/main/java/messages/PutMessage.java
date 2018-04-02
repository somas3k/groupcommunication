package messages;

import java.util.Map;

public class PutMessage extends MapMessage {
    public PutMessage(String key, String value) {
        super(key, value);
    }

    @Override
    public void action(Map<String, String> state) {
        state.put(key, value);
    }
}
