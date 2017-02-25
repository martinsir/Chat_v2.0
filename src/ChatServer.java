import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by Martin H on 22-02-2017.
 */

public class ChatServer implements Runnable {


    private List<ChatServerThread> clients = new ArrayList<>();
    private ServerSocket serverSocket = null;
    private Thread thread = null;


    public ChatServer(int port) {
        try {

            System.out.println("Binding to port " + port + ", please wait ...");
            serverSocket = new ServerSocket(port);
            System.out.println("Server started: " + serverSocket);
            this.thread = new Thread(this);
            this.thread.start();

        } catch (IOException ioe) {
            System.out.println("Port binding FAILED " + port + ": " + ioe.getMessage());
        }

    } // END ChatServer

    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for client ...");
                addClient(serverSocket.accept());
            } catch (IOException ioe) {
                System.err.println("Server/client link failed " + ioe.getMessage());

            }
        } // END while

    } // END run()

    public synchronized void handle(String id, String input) {

        for (ChatServerThread client : clients) {
            client.send(id + ": " + input);
        }
        System.out.println("ID " + id + "\nMSG: " + input + "\n");
    } // END handle()

    public void addClient(Socket socket) {
        System.out.println("Client accepted: " + socket);
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

    }

    public List<ChatServerThread> getClients() {
        return clients;
    }

    public static void main(String args[]) {
        new ChatServer(1234);
    }
}
