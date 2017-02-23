import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by Martin H on 23-02-2017.
 */

public class ChatClientGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/chat/chatRoom.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
        // send request to server
    }

    public static void main(String[] args) {
        launch();
    }
}

