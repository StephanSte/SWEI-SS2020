package at.technikum_wien.if18b070;

import at.technikum_wien.if18b070.Service.DBService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;


public class Main extends Application {
    public static File BILDER = new File("./src/main/resources/at/technikum_wien/if18b070/bilder/");
    //DB here
    public static DBService DATABASE;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("Fotodatenbank");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        DATABASE = new DBService();
        launch(args);
    }

}