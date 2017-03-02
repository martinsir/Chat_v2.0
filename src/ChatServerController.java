import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Martin H on 01-03-2017.
 */
public class ChatServerController implements Initializable, Runnable {

    @FXML
    private Button startServer;
    @FXML
    private ComboBox<ChatServerThread> comboBoxClients;
    @FXML
    private TextField port;
    @FXML
    private TextArea presentationTextAreaServer;
    @FXML
    private TextArea onlineUsersTextAreaServer;

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    private List<ChatServerThread> clients = new ArrayList<>();
    private ServerSocket serverSocket = null;
    private Thread thread = null;
    //private ChatServerThread chatServerThread;
    private boolean connected = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        presentationTextAreaServer.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onlineUsersServer();
            }
        });
    }

    public void startServer() {

        if (connected == false) {
            try {
                serverSocket = new ServerSocket(1234);
                this.thread = new Thread(this);
                this.thread.start();
                connected = true;
                startServer.setDisable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            startServer.setDisable(true);
        }
    }

    @Override
    public void run() {

        while (connected == true) {

            try {
                sendOnlineUsers();
                presentationTextAreaServer.appendText("Server started \n" + serverSocket + "\n");
                presentationTextAreaServer.appendText("Waiting for client...\n");
                addClient(serverSocket.accept());
            } catch (IOException ioe) {
                System.err.println("Server/client link failed " + ioe.getMessage());
            }
        } // END while
    }

    public void addClient(Socket socket) {
        presentationTextAreaServer.appendText("Client accepted " + socket + "\n");
        ChatServerThread serverThread = new ChatServerThread(this, socket);
        clients.add(serverThread);
        try {
            serverThread.open();
            serverThread.start();

        } catch (IOException ioe) {
            System.out.println("Thread ERR: ");
            ioe.printStackTrace();
            System.exit(-1);
        }
        sendOnlineUsers();
    } // END addClient

    public void sendOnlineUsers() {
// Create Loop to collect names of clients
        String clientList = "online#";
        for (ChatServerThread client : clients) {
            clientList += client.getClientName() + "\n";
        }
        // Create Loop to send message to client with list
        for (ChatServerThread client : clients) {
            client.send(clientList);
        }
        onlineUsersTextAreaServer.setText(clientList);
    }

    public void onlineUsersServer(){
        String clientList = "online#";
        for (ChatServerThread client : clients) {
            clientList += client.getClientName() + "\n";
        }
        comboBoxClients.getItems().clear();
        comboBoxClients.getItems().addAll(clients);
        onlineUsersTextAreaServer.setText(clientList);
    }


    public synchronized void handle(String id, String input) {
        for (ChatServerThread client : clients) {
            client.send(id + ": " + input);
        }
        presentationTextAreaServer.appendText("Server recorded: \n" + "ID: " + id + " Input: " + input + "\n");
    } // END handle()

    public List<ChatServerThread> getClients() {
        return clients;
    }

    public void kickButton() throws Exception {
        //boolean isMyComboBoxEmpty = myComboBox.getSelectionModel().isEmpty();
        if (!comboBoxClients.getSelectionModel().isEmpty())
        comboBoxClients.getValue().getSocket().close();
        //comboBoxClients.getItems().remove(comboBoxClients.getValue().getServer().getClients().remove(1));
        comboBoxClients.getValue().getServer().getClients().remove(comboBoxClients.getValue());
        onlineUsersServer();
    }

    public void comboBoxClients() {
        sendOnlineUsers();
    }

}