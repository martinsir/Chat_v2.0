import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Martin H on 25-02-2017.
 */

// https://app.asana.com/0/280864121249369/list
/// FIX heart beat
    /// FIX When GUI shutting down - catch exception on server
    /// Remember the Server GUI

public class HeartbeatMessage extends Thread {
    private final ChatClientController clientController;
//    private DataOutputStream dump = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

    public HeartbeatMessage(ChatClientController clientController) throws IOException {
        this.clientController = clientController;
    }

    public void run() {

        while (clientController.getSocket().isConnected() ==true) {
            String formatDateTime = dateTime();
            try {
                clientController.getStreamOut().writeUTF(formatDateTime+" "+"Sending a HeartBeat#");
                clientController.getStreamOut().flush();
                sleep(10000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    } // END run

    private String dateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); //dd-MM-yyyy
        return now.format(formatter);
    }

}




