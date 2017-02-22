import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Martin H on 22-02-2017.
 */
public class ChatClientThread extends Thread{

    private Socket socket = null;
    private ChatClient client = null;
    private DataInputStream inStream;// deprecated method

    public ChatClientThread(ChatClient chatClient, Socket socket) {

        this.client = chatClient;
        this.socket = socket;
        open();
        start();

    } // END ChatClientThread

    public void open() {
        try {
            inStream = new DataInputStream(socket.getInputStream());
        }catch (IOException ioe) {
            System.err.println("ERR input"+ioe.getMessage());
        }
    } // END open()

    public void run() {
        while(true) {
            try {
                client.handle(inStream.readUTF());
            }catch (IOException ioe) {
                System.err.println("Read ERR: "+ioe.getMessage());
                System.exit(-1);
            }
        }
    } // END run()

}
