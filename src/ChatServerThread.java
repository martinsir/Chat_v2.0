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
            String inputC;
            try {  // message from client
                inputC = inStream.readUTF();
                // check if its a command

                if (inputC.startsWith("username#")) {
                    clientChangeUserName(inputC);
                } else if (inputC.equals("QUIT#")) {
                    System.out.println(inputC +" ---- THIS IS THE QUIT CMD AND GETTING A UNIQUE IDENTIFIER "+ clientName );
                    String exitString;
                    exitString = inputC.replace("QUIT#", "Logged off%");
                    server.handle(clientName, exitString);
                    server.getClients().remove(this);
                    server.sendOnlineUsers();
                    inStream.close(); ////////////////////// get specific client
                    streamOut.close(); /////////////////////
                    socket.close(); ///////////////////////
                    break;
                } else {
                    server.handle(clientName, inputC);
                    streamOut.flush();
                }
            } catch (IOException e) {
                System.out.println("GUI LUKKER NED ");
                e.printStackTrace();
                try {
                    socket.close(); // ///////////////////
               } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            }

        }//END while


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