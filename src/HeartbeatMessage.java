import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Martin H on 25-02-2017.
 */

// https://app.asana.com/0/280864121249369/list
/// FIX heart beat
    /// FIX When GUI shutting down - catch exception on server
    /// Remember the Server GUI

public class HeartbeatMessage extends Thread {

//    private static long resetMsg = 1000; // 10 min -600.000
//    private DatagramPacket heartBeatMsg;
    private DataOutputStream streamOut = null;
    private final ChatClientController clientController;
    ///join a multicast grp and send the grp salutations
    String msg = "Wave at the server";
    InetAddress group = InetAddress.getByName("224.1.2.3");
    MulticastSocket mcs = new MulticastSocket(1234); // UDP connection
    DatagramPacket hiPacket = new DatagramPacket(msg.getBytes(), msg.length(), group, 1234);
    //get their responses
    byte[] buf = new byte[1000];
    DatagramPacket recv = new DatagramPacket(buf,buf.length);




    public HeartbeatMessage(ChatClientController clientController) throws IOException {
//        this.heartBeatMsg = heartBeatMsg;
        this.clientController = clientController;
    }

    public void run() {

        try {
            mcs.joinGroup(group);
            mcs.send(hiPacket);
            mcs.receive(recv);
            //ok, I'm done talking - leave the group
           // mcs.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} // end run





