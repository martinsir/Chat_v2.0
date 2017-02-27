import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

/**
 * Created by Martin H on 25-02-2017.
 */

// https://app.asana.com/0/280864121249369/list
/// FIX heart beat
    /// FIX When GUI shutting down - catch exception on server
    /// Remember the Server GUI

public class HeartbeatMessage extends Thread {
    private final ChatClientController clientController;
    private Socket socket = new Socket();
//    private DataOutputStream dump = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

    public HeartbeatMessage(ChatClientController clientController) throws IOException {
        this.clientController = clientController;
    }

    public void run() {

//        while (clientController.getSocket().isConnected() ==true) {
//            try {
//               // clientController.getStreamOut().writeUTF(clientController.getChosenUserName()+"It's ALIVE#");
//               // dump.writeUTF("Bump");
//                //clientController.getStreamOut().flush();
//                sleep(10000);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
} // end run





