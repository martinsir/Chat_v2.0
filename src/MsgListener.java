import javafx.scene.control.TextArea;
import java.io.DataInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Martin H on 23-02-2017.
 */

public class MsgListener implements Runnable {

    private DataInputStream inStream;
    private TextArea textArea;
    private TextArea textAreaOnline;

    public MsgListener(DataInputStream inStream, TextArea textArea, TextArea textAreaOnline) {
        this.inStream = inStream;
        this.textArea = textArea;
        this.textAreaOnline = textAreaOnline;
    }

    @Override
    public void run() {

        while (true) {
            String formatDateTime = getStringDateTime();
            try {
                String msg = inStream.readUTF();
                System.out.println(msg);

                if (msg.contains("Logged off%")) {
                    msg = msg.substring(msg.indexOf("#") + 1);
                    msg = msg.replace("%","");
                }
                //5 1 3 7 4 : online#Martin
                if (msg.contains("online#")) {
                    msg = msg.substring(msg.indexOf("#") + 1, msg.length());

                    // add to online user textArea
                    textAreaOnline.setText(msg);

                } else if (!msg.isEmpty()){
                    textArea.appendText(formatDateTime+ "  "+msg + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } //END run()

    private String getStringDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); //dd-MM-yyyy
        return now.format(formatter);
    }
}