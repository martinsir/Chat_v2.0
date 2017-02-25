import javafx.scene.Scene;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
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

        // send heartbeat every 10 min, else, kill client- send to server

        try {
            while (SocketChannel.open().isOpen()) {
                String formatDateTime = getStringDateTime();
                try {
                    String msg = inStream.readUTF();
                    System.out.println(msg);
                    if (msg.contains("Logged off%")) {
                        msg = msg.substring(msg.indexOf("#") + 1);
                        msg = msg.replace("%", "");
                    }
                    if (msg.contains("SERVER: username")) {

                        //send red flag to gui

                    }
                    //5 1 3 7 4 : online#Martin
                    if (msg.contains("online#")) {
                        msg = msg.substring(msg.indexOf("#") + 1, msg.length());
                        // add to online user textArea
                        textAreaOnline.setText(msg);
                    } else if (!msg.isEmpty()) {
                        textArea.appendText(formatDateTime + "  " + msg + "\n");
                    }
                } catch (EOFException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //END run()

    private String getStringDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); //dd-MM-yyyy
        return now.format(formatter);
    }

}