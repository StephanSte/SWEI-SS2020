package at.technikum_wien.if18b070;

/*
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;*/

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
    public Connection connection;
    public  Connection getConnection(){

        String dbName="SWEIDB.db";
        try {
            Class.forName("ConnectionClass").getDeclaredConstructor().newInstance();

            connection= DriverManager.getConnection("jdbc:sqlite:"+dbName);



        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
