import messages.MapMessage;
import org.jgroups.*;
import org.jgroups.util.Util;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Receiver extends ReceiverAdapter {
    private DistributedMap state;
    private JChannel channel;

    public Receiver(DistributedMap state, JChannel channel) {
        this.state = state;
        this.channel = channel;
    }

    @Override
    public void receive(Message msg) {
        MapMessage message = (MapMessage) msg.getObject();
        message.action(state.getHashMap());
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        Util.objectToStream(new HashMap<>(state.getHashMap()), new DataOutputStream(new BufferedOutputStream(output)));
    }

    @Override
    public void setState(InputStream input) throws Exception {
        HashMap<String, String> map = (HashMap<String, String>) Util.objectFromStream(new DataInputStream(new BufferedInputStream(input)));
        state.clear();
        state.addAll(map);
    }

    @Override
    public void viewAccepted(View view) {
        if(view instanceof MergeView){
            MergeView tmp = (MergeView) view;
            ViewHandler handler = new ViewHandler(channel, tmp);
            handler.start();

        }
        System.out.println("** view: " + view);
    }

    private static class ViewHandler extends Thread {
        JChannel ch;
        MergeView view;

        private ViewHandler(JChannel ch, MergeView view) {
            this.ch = ch;
            this.view = view;
        }

        public void run() {
            List<View> subgroups = view.getSubgroups();
            View tmp_view = subgroups.get(0); // picks the first
            Address local_addr = ch.getAddress();
            if (!tmp_view.getMembers().contains(local_addr)) {
                System.out.println("Not member of the new primary partition ("
                        + tmp_view + "), will re-acquire the state");
                try {
                    ch.getState(tmp_view.getCoord(), 30000);
                } catch (Exception ex) {
                }
            } else {
                System.out.println("Not member of the new primary partition ("
                        + tmp_view + "), will do nothing");
            }
        }
    }
}
