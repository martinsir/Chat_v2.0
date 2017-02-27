import java.io.*;
import java.net.Socket;

/**
 * Created by Martin H on 22-02-2017.
 */

public class ChatServerThread extends Thread{

    private ChatServer server = null;
    private Socket socket = null;
    private DataInputStream inStream = null;
    private DataOutputStream streamOut = null;
    private String clientName;
    private boolean serverON = true;

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
        while (serverON) {
            try {
                // message from client
                String input = inStream.readUTF(); // CATCH EXCEPTION

                // check if its a command

                if (!serverON) {
                    streamOut.flush();
                    streamOut.close();
                    inStream.close();
                    serverON=false;
                }


                if (input.startsWith("username#")) {
                    clientChangeUserName(input);
                } else if (input.startsWith("QUIT#")) {
                    String exitString;
                    exitString = input.replace("QUIT#", "Logged off%");
                    server.handle(clientName, exitString);
                   serverON=false;

                    /////////Keeep SERVER ALIVE AND CLOSE CLIENT

                } else {
                    server.handle(clientName, input);
                    streamOut.flush();
                }
            } catch (InterruptedIOException iioe) {
                iioe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);

            }
        } //END while

    }// END run

    public void clientChangeUserName(String input) {
        boolean nameTaken = false;
        String desiredUserName = input.replace("username#", "");

        for (ChatServerThread client : server.getClients()) {
            if (client.getClientName().equalsIgnoreCase(desiredUserName)) {
                nameTaken = true;
            }
        }
        if (nameTaken) {
            // do something when name is taken
            send("SERVER: username \"" + desiredUserName + "\" is already taken.");

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