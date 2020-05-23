package at.technikum_wien.if18b070;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;


public class HelloFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        /*String path = "Test.jpg";
        File insert = new File("\"/com/bar/resources/at/technikum_wien/if18b070/bilder/\""+path);
        Image image = new Image(insert.toURI().toString());
        ImageView iv = new ImageView(image);
        root.getChildren().add(iv);*/

        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}