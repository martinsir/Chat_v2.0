import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin H on 22-02-2017.
 */

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    private DataInputStream inStream = null;
    private DataOutputStream streamOut = null;
    private String clientName;
    private boolean dubName;
    private List<ChatServerThread> clients = new ArrayList<>();

    public ChatServerThread(ChatServer server, Socket socket) {

        this.server = server;
        this.socket = socket;
        this.clientName = String.valueOf(socket.getPort());

    } // END ChatServerThread

    public void send(String message) {

        try {
            streamOut.writeUTF(message);
            streamOut.flush();

        } catch (IOException ioe) {
            System.err.println(clientName + "ERR sending: " + ioe.getMessage());
        }
    } // END send()

    public void run() {
        System.out.println("Server Thread " + clientName + " running.");
        while (true) {
            try {
                String input = inStream.readUTF(); // message from client
                // check if its a command
                if (input.startsWith("username#")) {
                    this.clientName = input.replace("username#", "");
                    dubName = true;
                    //compare to method
                    if (dubName = true) {
                        for (int i = 0; i < clients.size(); i++) {
                            for (int k = i + 1; k < clients.size(); k++)
                                if (k != i && clients.get(k) == clients.get(i)){
                                //name taken ?
                                }

                        }
                    } else {
                        server.sendOnlineUsers();
                        System.out.println("Client is now known as " + clientName);
                    }
                } else if (input.startsWith("QUIT#")) {
                    String exitString;
                    exitString = input.replace("QUIT#", "Logged off%");
                    server.handle(clientName, exitString);
                } else {
                    server.handle(clientName, input);
                }

            } catch (IOException ioe) {
                System.err.println(clientName + " ERR reading: " + ioe.getMessage());
                System.exit(-1);
            }


        } //END while

    }// END run

    public String getClientName() {
        return clientName;
    }


    public void open() throws IOException {
        inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

}
