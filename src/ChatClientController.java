import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Martin H on 23-02-2017.
 */

public class ChatClientController {

    private Socket socket = null;
    private DataInputStream inStream = null;
    private DataOutputStream streamOut = null;

    @FXML
    private TextArea writeMessageTextArea;
    @FXML
    private TextField host;
    @FXML
    private TextField port;
    @FXML
    private Button connect;
    @FXML
    private Button sendMessage;
    @FXML
    private TextArea presentationTextArea;
    @FXML
    private TextArea onlineUsersTextArea;

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();

    }

    public void connectToServer() {
        System.out.println("Connecting. Please wait...");
        try {
            socket = new Socket(host.getText(), Integer.parseInt(port.getText()));
            System.out.println("Connected: " + socket);
            streamOut = new DataOutputStream(socket.getOutputStream());
            MsgListener listener = new MsgListener(new DataInputStream(socket.getInputStream()), presentationTextArea, onlineUsersTextArea);
            Thread t1 = new Thread(listener);
            t1.start();

        } catch (UnknownHostException uhe) {
            System.err.println("Unknown Host : " + uhe.getMessage());
        } catch (IOException ioe) {
            System.err.println("ERR: " + ioe.getMessage());
        }
    }


    public void sendMessage(ActionEvent actionEvent) {

        try {
            streamOut.writeUTF(writeMessageTextArea.getText());
            streamOut.flush();
            writeMessageTextArea.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLogout(ActionEvent actionEvent) {
        try {
            streamOut.writeUTF("QUIT#"); //.......
            streamOut.flush();
            // System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
