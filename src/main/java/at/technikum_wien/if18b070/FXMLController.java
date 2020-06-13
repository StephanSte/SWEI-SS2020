package at.technikum_wien.if18b070;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.util.*;

import at.technikum_wien.if18b070.Models.MockPictureModels;
import at.technikum_wien.if18b070.Models.PictureModel;
import at.technikum_wien.if18b070.PresentationModels.PictureViewModel;
import at.technikum_wien.if18b070.Service.DBService;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import javax.swing.JFileChooser;
import java.io.File;
import java.util.List;


public class FXMLController implements Initializable {

    @FXML
    private Label label;
    @FXML
    public TextField textField;
    @FXML
    public Label textLabel;
    @FXML
    public MenuBar topMenuBar;
    @FXML
    public TextField searchBar;
    @FXML
    public ImageView imgActive;
    @FXML
    public AnchorPane imgActiveContainer;
    @FXML
    public HBox imgScrollPaneHBox;
    @FXML
    private Button button;
    @FXML
    private MenuItem ChooseFile;


    private PictureViewModel PictureViewModel;
    private List<PictureViewModel> ListofModels = new ArrayList<PictureViewModel>();
    //private List<PictureModel> pictures;



    private void prep(){
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        this.label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
        // img
        this.imgActive.fitWidthProperty().bind(this.imgActiveContainer.widthProperty());
        this.imgActive.fitHeightProperty().bind(this.imgActiveContainer.heightProperty());
    }

    //**************************************** loadAllPictures ******************************************************
    private void loadPicturesFromDB(){
        //TODO: make this reality
        //DBService database = new DBService();
        //database.getAllPictures();
    }

    //private void loadPicturesFromMock(){ this.pictures = new MockPictureModels().getPictureModels(); }

    private void loadAllPictures(){
        //DBService getAllPictures
        for(String path: new MockPictureModels().getAllPaths()){
            ListofModels.add(new PictureViewModel(new PictureModel(path)));
        }
    }
    
    //****************************** Fill Scroll Pane with all Pictures *******************************************
    private void fillScrollPane() {

        for(PictureViewModel pvm : ListofModels) {

            //System.out.println(pvm.getPath());

            Image img = new Image("file:" + pvm.path.getValue());
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(200);
            imgView.setPreserveRatio(true);


            imgView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                //set new PictureModel for the active ViewModel
                PictureViewModel.setPictureModel(new PictureModel(pvm.path.getValue()));
                PictureViewModel.updateProperties();

                updateActiveImage();
                event.consume();
            });
            imgScrollPaneHBox.getChildren().add(imgView);
        }
    }

    //*********************** create and update Active Picture that is displayed *************************************
    private void initializeActivePicture() {

        // set activePictureViewModel
        PictureViewModel = new PictureViewModel(
                // create new PictureModel
                new PictureModel(ListofModels.get(0).path.getValue())
        );

        updateActiveImage();
    }

    private void updateActiveImage(){
        Image image = null;
        try {
            image = new Image(new FileInputStream(PictureViewModel.path.getValue()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imgActive.setImage(image);
    }

    //TODO: change to DBService connection
    //***************************************put here temporarily*******************************************************
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
        prep();
        //Connection here?

        loadAllPictures();
        //loadPicturesFromMock();

        fillScrollPane();
        initializeActivePicture();
    }

}