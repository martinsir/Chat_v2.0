import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Martin H on 23-02-2017.
 */

public class MsgListener implements Runnable {

    private final ChatClientController clientController;
    private DataInputStream inStream;


    public MsgListener(ChatClientController clientController) {
        this.inStream = clientController.getInStream();
        this.clientController = clientController;

    }

    @Override
    public void run() {

        try {
            while (true) {
                String formatDateTime = dateTime();
                try {
                    String msg = inStream.readUTF();
                    System.out.println(msg);

                    if(msg.contains("Sending a HeartBeat#")) {
                        msg = msg.substring(msg.indexOf("#")+1);

                    }
                    if (msg.contains("Logged off%")) {
                        msg = msg.substring(msg.indexOf("#") + 1);
                        msg = msg.replace("%", "");
                        this.clientController.getSocket().close();
                    }
                    if (msg.contains("SERVER: username")) {
                        clientController.setNameTaken(true);
                    } else {
                        clientController.setNameTaken(false);
                    }
                    //5 1 3 7 4 : online#Martin
                    if (msg.contains("online#")) {
                        msg = msg.substring(msg.indexOf("#") + 1, msg.length());
                        // add to online user textArea
                        clientController.getOnlineUsersTextArea().setText(msg);
                    } else if (!msg.isEmpty()) {
                        clientController.getPresentationTextArea().appendText(formatDateTime + "  " + msg + "\n");
                    }
                } catch (EOFException e) {
                    e.printStackTrace();
                    System.out.println(e.getCause().getMessage());
                    System.exit(-1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //END run()

    private String dateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); //dd-MM-yyyy
        return now.format(formatter);
    }

}