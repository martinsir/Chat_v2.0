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
                    this.clientName = input.replace("username#", "");
                    server.sendOnlineUsers();
                    System.out.println("Client is now known as " + clientName);
                } else {
                    server.handle(clientName, input);
                }
                if (input.startsWith("QUIT#")) {///////////////////////
                    //socket.sendUrgentData(Integer.parseInt(input));
                    input.replace("QUIT#", " Logging off"); /////////////
                }
            } catch (IOException ioe) {
                System.err.println(clientName + " ERR reading: " + ioe.getMessage());
                System.exit(-1);
            }
        }
    }// END run()

    public String getClientName() {
        return clientName;
    }

    public void open() throws IOException {
        inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

}
