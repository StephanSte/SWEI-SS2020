package at.technikum_wien.if18b070;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    @FXML
    public ImageView image;
    @FXML
    public AnchorPane rightSide;
    @FXML
    private Label label;

    public HashMap<String,String> IPTC;
    public HashMap<String,String> EXIF;
    public TextField textField;
    public Label textLabel;


    //**************************************DB Functions**********************************************************
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:SWEIDB.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(ActionEvent event) {
        createNewTable();

        String insertName = textField.getText();
        textLabel.setText(textField.getText());
        //InputStream in = getClass().getResourceAsStream("/com/bar/resources/at/technikum_wien/if18b070/bilder"+path);

        int insertId = 0;

        String sql = "INSERT INTO photographer (id,name) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //pstmt.setInt(1, insertId);
            pstmt.setString(2, insertName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:SWEIDB.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS photographer ("
                + " id integer PRIMARY KEY,"
                + " name text NOT NULL,"
                + " ppath text"
                + " );";

        String IPTC = "CREATE TABLE IF NOT EXISTS ITPC (\n"
                + "id integer PRIMARY KEY,\n"
                + "thing1 text ,\n"
                + "thing2 text ,\n"
                + "thing3 text\n"
                + ");";
        String EXIF = "CREATE TABLE IF NOT EXISTS ITPC (\n"
                + "id integer PRIMARY KEY,\n"
                + "thing1 text ,\n"
                + "thing2 text ,\n"
                + "thing3 text\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute(IPTC);
            stmt.execute(EXIF);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectAll(){
        String sql = "SELECT id, name,ppath FROM photographer";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("ppath"));

                textLabel.setText(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("ppath"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //***************************************Random Functions*******************************************************
    public void generateRandom(ActionEvent event){
        Random rand = new Random();
        int myrand = rand.nextInt(50) + 1;
        label.setText(Integer.toString(myrand));
    }

    public void button(ActionEvent actionEvent) throws SQLException {
        textLabel.setText(textField.getText());
        //System.out.println("Program is running");
    }

    //*****************************************Initialisation**************************************************************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");

        //image.fitHeightProperty().bind(rightSide.heightProperty());
        //image.fitWidthProperty().bind(rightSide.widthProperty());
    }
}