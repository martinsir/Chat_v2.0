import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Martin H on 25-02-2017.
 */

/// Remember the Server GUI

public class HeartbeatMessage extends Thread {
    private final ChatClientController clientController;

    public HeartbeatMessage(ChatClientController clientController) throws IOException {
        this.clientController = clientController;
    }

    public void run() {

        while (this.clientController.getSocket().isConnected()) {
            String formatDateTime = dateTime();
            try {
                clientController.getStreamOut().writeUTF(formatDateTime + " " + "Sending a HeartBeat#");
                clientController.getStreamOut().flush();
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();               ///// Kill thread - it's still sending on a closed socket
                try {
                    this.clientController.getSocket().close();
                    break;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    } // END run

    private String dateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); //dd-MM-yyyy
        return now.format(formatter);
    }
}
