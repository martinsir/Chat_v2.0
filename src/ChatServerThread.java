import java.io.*;
import java.net.Socket;

/**
 * Created by Martin H on 22-02-2017.
 */
public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket =null;
    private int iD = -1;
    private DataInputStream inStream =null;
    private DataOutputStream streamOut =null;


    public ChatServerThread(ChatServer server, Socket socket) {

        //super();
        this.server = server;
        this.socket = socket;
        this.iD = socket.getPort();

    } // END ChatServerThread

    public void send(String message) {

        try {
            streamOut.writeUTF(message);
            streamOut.flush();

        }catch (IOException ioe){
            System.err.println(iD+"ERR sending: "+ioe.getMessage());
        }
    } // END send()

    public void run() {
        System.out.println("Server Thread "+iD+" running.");
        while (true) {
            try {
                server.handle(iD,inStream.readUTF());
            }catch (IOException ioe) {
                System.err.println(iD+" ERR reading: "+ioe.getMessage());
                System.exit(-1);
            }
        }
    }// END run()

    public void open() throws IOException{
        inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

}
