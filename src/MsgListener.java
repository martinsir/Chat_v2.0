import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.IOException;

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
            try {
                String msg = inStream.readUTF();
                System.out.println(msg);

                if (msg.contains("QUIT#")) {
                    msg = msg.substring(msg.indexOf("#") + 1, msg.length());
                    //textArea.appendText(" Logged off");//........
                    //textArea.appendText(" Logged off"); .........
                }

                //5 1 3 7 4 : online#Martin
                if (msg.contains("online#")) {
                    msg = msg.substring(msg.indexOf("#") + 1, msg.length());

                    // add to online user textArea
                    textAreaOnline.setText(msg);

                } else {
                    textArea.appendText(msg + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
