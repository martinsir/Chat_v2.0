import java.io.*;
import java.net.Socket;

/**
 * Created by Martin H on 22-02-2017.
 */

public class ChatServerThread extends Thread {

    private ChatServerController server = null;
    private Socket socket = null;
    private DataInputStream inStream = null;
    private DataOutputStream streamOut = null;
    private String clientName;
    boolean nameTaken;


    public ChatServerThread(ChatServerController server, Socket socket) {

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
        while (!socket.isClosed()) {
            String inputC;
            try {  // message from client
                inputC = inStream.readUTF();
                // check if its a command
                if (inputC.startsWith("username#")) {
                    clientChangeUserName(inputC);
                } else if (inputC.equals("QUIT#")) {
                    System.out.println("this is the input from: "+ inputC);
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
                System.out.println("GUI SHUTTING DOWN");
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
        nameTaken=false;
        String desiredUserName = input.replace("username#", "");
        System.out.println("desired name entered");

        for (ChatServerThread client : server.getClients()) {
            System.out.println("running for each name ");
            if (client.getClientName().equalsIgnoreCase(desiredUserName)) {
                System.out.println(" names getting compared ");
                nameTaken = true;
                System.out.println("name sat to taken ");

            }
        }
        if (nameTaken == true) {
            System.out.println("name is taken ");
            send("SERVER: username \"" + desiredUserName + "\" is already taken.");
        }
        if (nameTaken == false) {
            System.out.println("name is not taken ");
            clientName = desiredUserName;
            server.sendOnlineUsers();
            System.out.println("Client is now known as " + clientName);
        }
    }

    @Override
    public String toString() {
        return  clientName;
    }

    public String getClientName() {
        return clientName;
    }


    public void open() throws IOException {
        inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public ChatServerController getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }
}