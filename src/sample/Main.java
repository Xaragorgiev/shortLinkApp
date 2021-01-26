package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Сокращение ссылок");
        primaryStage.setScene(new Scene(root, 730, 300));
        primaryStage.show();

        primaryStage.setOnCloseRequest(we -> {
            try {
                FileOutputStream fos = new FileOutputStream("ControllerHelp.settings");
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(new ControllerHelp("", "", "", ""));

                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
