import java.io.DataInputStream; // deprecated method - rewrite BufferedReader // needs a readMethod
import java.io.DataOutputStream; // deprecated method - rewrite PrintWriter
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Martin H on 22-02-2017.
 */

public class ChatClient implements Runnable{

    private Socket socket =null;
    private Thread thread = null;
    private DataInputStream inStream = null;    // deprecated method
    private DataOutputStream streamOut = null; // deprecated method
    private ChatClientThread client =null;

    public ChatClient(String serverName, int serverPort) {
        System.out.println("Connecting. Please wait...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: "+ socket);
            start();
        }catch (UnknownHostException uhe) {
            System.err.println("Unknown Host : "+uhe.getMessage());
        }catch (IOException ioe) {
            System.err.println("ERR: "+ioe.getMessage());
        }
    }// END ChatClient


    public void run(){
        while (thread != null) {
            try{

                streamOut.writeUTF(inStream.readLine()); // deprecated method - rewrite /if read - needs a read method
                streamOut.flush();

            }catch (IOException ioe) {
                System.err.println("ERR sending : "+ioe.getMessage());
            }
        }
    }// END run()


    public void handle(String message) {
        System.out.println(message);
    }


    public void start() throws IOException {
        inStream = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if (thread == null) {
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }// END start()

    public static void main(String args[])  {

        new ChatClient("",1234);
        //launch(args);

    }


        //@Override
        //public void start(Stage primaryStage) throws Exception {
        //   Parent root = FXMLLoader.load(getClass().getResource("/chat.fxml/chatRoom.fxml"));
        //  primaryStage.setTitle("Chat");
        //  primaryStage.setScene(new Scene(root, 600, 400));
        //  primaryStage.show();
        // }
} // END Class
