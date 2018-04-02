package messages;

import java.util.Map;

public class RemoveMessage extends MapMessage {
    public RemoveMessage(String key) {
        super(key);
    }

    @Override
    public void action(Map<String, String> state) {
        state.remove(key);
    }
}
