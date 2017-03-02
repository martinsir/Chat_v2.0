/**
 * Created by Martin H on 01-03-2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChatServerGUI extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/chat/chatServer.fxml"));
        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
