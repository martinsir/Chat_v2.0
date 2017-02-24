import java.io.*;
import java.net.Socket;

/**
 * Created by Martin H on 22-02-2017.
 */

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    private DataInputStream inStream = null;
    private DataOutputStream streamOut = null;
    private String clientName;

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
                    clientChangeUserName(input);
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


    public void clientChangeUserName(String input) {
        boolean nameTaken = false;
        String desiredUserName = input.replace("username#", "");

        for (ChatServerThread client : server.getClients()) {
            if (client.getClientName().toLowerCase().equals(desiredUserName.toLowerCase())) {
                nameTaken = true;
            }

        }
        if (nameTaken) {
            // do something when name is taken
            System.out.println("Username " + desiredUserName + " is already taken");
        } else {
            this.clientName = desiredUserName;
            server.sendOnlineUsers();
            System.out.println("Client is now known as " + clientName);
        }
    }


    public String getClientName() {
        return clientName;
    }


    public void open() throws IOException {
        inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }
}
