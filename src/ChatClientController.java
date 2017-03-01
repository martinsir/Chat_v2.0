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

import static java.lang.Thread.sleep;


/**
 * Created by Martin H on 23-02-2017.
 */

public class ChatClientController implements Initializable {
    private Socket socket = null;
    private DataInputStream inStream = null;
    private DataOutputStream streamOut = null;
    private boolean nameTaken = false;
    private int maxCharPressed = 250;
    private String chosenUserName;


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
    private Button usernameButton;
    @FXML
    private TextField usernameTxtField;
    @FXML
    private Label maxChar;
    @FXML
    private Label charLeft;


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

        maxChar.setVisible(false);
        charLeft.setVisible(false);
        usernameTxtField.setVisible(false);
        usernameButton.setVisible(false);
        sendMessage.setVisible(false);
        writeMessageTextArea.setVisible(false);
        logout.setVisible(false);
        connect.setVisible(true);
    }

    public void connectToServer() {
        System.out.println("Connecting. Please wait...");
        try {
            socket = new Socket(host.getText(), Integer.parseInt(port.getText()));
            System.out.println("Connected: " + socket);
            streamOut = new DataOutputStream(socket.getOutputStream());
            inStream = new DataInputStream(socket.getInputStream());
            MsgListener listener = new MsgListener(this);
            Thread t1 = new Thread(listener);
            HeartbeatMessage hbm = new HeartbeatMessage(this);
            Thread t2 = new Thread(hbm);
            t2.start();
            t1.start();
            connectedUI();
        } catch (UnknownHostException uhe) {
            System.err.println("Unknown Host : " + uhe.getMessage());
        } catch (IOException ioe) {
            System.err.println("ERR: " + ioe.getMessage());
        }
        if (!connect.isVisible()) {
            usernameButton.setVisible(true);
            usernameTxtField.setVisible(true);
            sendMessage.setDisable(false);
            writeMessageTextArea.setDisable(false);
            host.setVisible(false);
            port.setVisible(false);
            jLhost.setVisible(false);
            jLport.setVisible(false);
            checker();
        }
    } ////////// END connectToServer

    public void checker() {
        maxChar.setText(writeMessageTextArea.getText().length() + "/ 250");
        if (!usernameButton.isVisible()) {
            maxChar.setVisible(true);
            charLeft.setVisible(true);
            sendMessage.setVisible(true);
            writeMessageTextArea.setVisible(true);
        } else {
            connect.setVisible(false);
            host.setVisible(false);
            port.setVisible(false);
            jLhost.setVisible(false);
            jLport.setVisible(false);
        }

        if (writeMessageTextArea.getText().length() > maxCharPressed || writeMessageTextArea.getText().isEmpty()) {
            sendMessage.setDisable(true);
        } else {
            sendMessage.setDisable(false);
        }
    }

    private void connectedUI() {
        connect.setVisible(false);
        logout.setVisible(true);
        host.setVisible(false);
        port.setVisible(false);
        jLhost.setVisible(false);
        jLport.setVisible(false);
    }

    public void sendMessage() {
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
            streamOut.writeUTF("QUIT#");
            streamOut.flush();
            exitApplication(actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void usernameButton() throws IOException {
        streamOut.writeUTF("username#" + usernameTxtField.getText());
        streamOut.flush();
        usernameButton.setDisable(true);

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (getNameTaken() == true) {
            usernameButton.setVisible(true);
            usernameTxtField.setVisible(true);
            presentationTextArea.appendText("Please choose another username.\n");
            usernameButton.setDisable(false);
            System.out.println("nametaken true");

        } else if (getNameTaken() == false) {
            System.out.println("nametaken false");
            usernameButton.setVisible(false);
            usernameTxtField.setVisible(false);
            writeMessageTextArea.setVisible(true);
            sendMessage.setVisible(true);
            maxChar.setVisible(true);
            charLeft.setVisible(true);
            getPresentationTextArea().appendText("Link: Success!\nWelcome to the chat "
                    + usernameTxtField.getText() + "\n");
            setChosenUserName(usernameTxtField.getText());
        }
    }


    public boolean getNameTaken() {
        return nameTaken;
    }

    public void setNameTaken(boolean nameTaken) {
        this.nameTaken = nameTaken;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getInStream() {
        return inStream;
    }

    public DataOutputStream getStreamOut() {
        return streamOut;
    }

    public TextArea getPresentationTextArea() {
        return presentationTextArea;
    }

    public TextArea getOnlineUsersTextArea() {
        return onlineUsersTextArea;
    }


    public void setChosenUserName(String chosenUserName) {
        this.chosenUserName = chosenUserName;
    }
}