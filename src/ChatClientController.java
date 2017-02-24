import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * Created by Martin H on 23-02-2017.
 */

public class ChatClientController implements Initializable{

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
    private Label jLport;
    @FXML
    private Label jLhost;
    @FXML
    private Button logout;
    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        writeMessageTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
           checker();
            }
        });

        connect.setVisible(true);
        sendMessage.setVisible(false);
        writeMessageTextArea.setVisible(false);
        logout.setVisible(false);

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
            logged();

        } catch (UnknownHostException uhe) {
            System.err.println("Unknown Host : " + uhe.getMessage());
        } catch (IOException ioe) {
            System.err.println("ERR: " + ioe.getMessage());
        }
        if(!connect.isVisible()) {
            sendMessage.setVisible(true);
            writeMessageTextArea.setVisible(true);

        }else {
            connect.setVisible(false);
            host.setVisible(false);
            port.setVisible(false);
            jLhost.setVisible(false);
            jLport.setVisible(false);
        }
    }

    public void checker() {
        if(writeMessageTextArea.getText().isEmpty()) {
            sendMessage.setDisable(true);
        }else {
            sendMessage.setDisable(false);
        }
    }

    private void logged() {
        connect.setVisible(false);
        logout.setVisible(true);
        host.setVisible(false);
        port.setVisible(false);
        jLhost.setVisible(false);
        jLport.setVisible(false);

    }

    public void sendMessage(ActionEvent actionEvent) {
        checker();
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

            exitApplication(actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}