import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private ChatServerThread chatServerThread;
    private boolean connected =false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void startServer() {

        if (connected==false) {
            try {
                serverSocket = new ServerSocket(1234);
                this.thread = new Thread(this);
                this.thread.start();
                connected=true;
                startServer.setDisable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            startServer.setDisable(true);
        }
    }

    @Override
    public void run() {
        while (thread != null) {

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
            comboBoxClients.getItems().clear();
            comboBoxClients.getItems().addAll(clients);

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

        comboBoxClients.getItems().clear();
        comboBoxClients.getItems().addAll(clients);
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

    public void kickButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        comboBoxClients.getValue().getServer().getClients().remove(this);
        comboBoxClients.getValue().getSocket().close();
    }

    public void comboBoxClients(ActionEvent actionEvent) {

    }

    public void presentationTextAreaServer(MouseEvent mouseEvent) {

    }

    public void writeCMDTextAreaServer(MouseEvent mouseEvent) {
    }

    public void cmdButton(ActionEvent actionEvent) {

    }
}
