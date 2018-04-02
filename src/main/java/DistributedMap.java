import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.ListView;
import messages.PutMessage;
import messages.RemoveMessage;
import org.jgroups.JChannel;
import org.jgroups.Message;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class DistributedMap implements SimpleStringMap, Serializable {
    private ObservableMap<String, String> hashMap;
    private JChannel channel;

    public DistributedMap(JChannel channel, ListView<String> listView){
        hashMap = FXCollections.observableHashMap();
        MapChangeListener<String, String> listener = c -> {
            if(c.wasRemoved()){
                Platform.runLater(() ->listView.getItems().remove(c.getKey()));
            }
            else{
                Platform.runLater(() ->listView.getItems().add(c.getKey()));
            }
        };
        hashMap.addListener(listener);
        this.channel = channel;
    }

    public DistributedMap(JChannel channel){
        hashMap = FXCollections.observableHashMap();
        this.channel = channel;
    }

    public synchronized void clear(){
        hashMap.clear();
    }

    @Override
    public boolean containsKey(String key) {
        return hashMap.containsKey(key);
    }

    @Override
    public synchronized String get(String key) {
        return hashMap.get(key);
    }

    @Override
    public synchronized String put(String key, String value) {
        String res = hashMap.put(key, value);
        try {
            channel.send(new Message(null, null, new PutMessage(key, value)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public synchronized void addAll(Map<String, String> map){
        hashMap.clear();
        hashMap.putAll(map);
    }

    public Set<String> keySet(){
        return hashMap.keySet();
    }

    @Override
    public synchronized String remove(String key) {
        String res = hashMap.remove(key);
        try{
            channel.send(new Message(null, null, new RemoveMessage(key)));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public Map<String, String> getHashMap() {
        return hashMap;
    }


}
