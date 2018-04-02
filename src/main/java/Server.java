import org.jgroups.JChannel;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK2;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.protocols.pbcast.STATE_TRANSFER;
import org.jgroups.stack.ProtocolStack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;

public class Server {
    private JChannel channel;
    private DistributedMap state;
    private MapManager manager;

    private void setProtocolStack(String address) throws Exception{
        ProtocolStack stack=new ProtocolStack();
        channel.setProtocolStack(stack);
        stack.addProtocol(new UDP().setValue("mcast_group_addr", Inet4Address.getByName(address)))
                .addProtocol(new PING())
                .addProtocol(new MERGE3())
                .addProtocol(new FD_SOCK())
                .addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
                .addProtocol(new VERIFY_SUSPECT())
                .addProtocol(new BARRIER())
                .addProtocol(new NAKACK2())
                .addProtocol(new UNICAST3())
                .addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new UFC())
                .addProtocol(new MFC())
                .addProtocol(new FRAG2())
                .addProtocol(new STATE_TRANSFER());
        stack.init();
    }

    public Server(String address, MapManager manager) {
        this.manager = manager;
        channel = new JChannel(false);

        state = new DistributedMap(channel, manager.getListView());
        try {
            setProtocolStack(address);
            channel.setDiscardOwnMessages(true);
            channel.setReceiver(new Receiver(state, channel));
            channel.connect("DistributedMap");
            channel.getState(null, 10000);
            //eventLoop();
            //channel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Server(String address){
        channel = new JChannel(false);

        state = new DistributedMap(channel);
        try {
            setProtocolStack(address);
            channel.setDiscardOwnMessages(true);
            channel.setReceiver(new Receiver(state, channel));
            channel.connect("DistributedMap");
            channel.getState(null, 10000);
            eventLoop();
            channel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eventLoop(){
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            try{
                System.out.print("Key: "); System.out.flush();
                String key = in.readLine();
                if(key.startsWith("quit") || key.startsWith("exit")) break;
                System.out.print("Value: "); System.out.flush();
                String value = in.readLine();
                state.put(key, value);
            }
            catch (Exception e){
            }

        }
    }

    public DistributedMap getState() {
        return state;
    }
}
