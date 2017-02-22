import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by Martin H on 22-02-2017.
 */


public class ChatServer implements Runnable{

    private List<ChatServerThread> clients = new ArrayList<>();
    private ServerSocket serverSocket = null;
    private Thread thread = null;

    public ChatServer(int port) {
        try {

            System.out.println("Binding to port "+port+", please wait ...");
            serverSocket = new ServerSocket(port);
            System.out.println("Server started: "+ serverSocket);
            this.thread = new Thread(this);
            this.thread.start();

        }catch (IOException ioe) {
            System.out.println("Port binding FAILED "+port+": "+ioe.getMessage());
        }

    } // END ChatServer

    public void run(){
        while (thread!=null) {
            try {
                System.out.println("Waiting for client ...");
                addClient(serverSocket.accept());
            }catch (IOException ioe) {
                System.err.println("Server/client link failed "+ioe.getMessage());
            }
        } // END while

    } // END run()

    public synchronized void handle(int iD, String input){    // Using DataInput/OutputSteam - may be necessary to implement syncronized on handle or make a blockqueue method

        for (ChatServerThread client : clients) {

            client.send(iD + ": "+ input);
        }
        System.out.println("ID "+iD+"\nMSG: "+input+"\n");
    } // END handle()

    public void addClient(Socket socket) {
        System.out.println("Client accepted: "+socket);
        ChatServerThread serverThread = new ChatServerThread(this, socket);
        clients.add(serverThread);
        try {
                      serverThread.open();
                      serverThread.start();
        }catch (IOException ioe) {
            System.err.println("Thread ERR: "+ioe.getMessage());
        }
    } // END addClient

    public static void main(String args[]){
        new ChatServer(1234);
    }
}
