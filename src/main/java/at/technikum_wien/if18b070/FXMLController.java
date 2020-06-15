package at.technikum_wien.if18b070;

import at.technikum_wien.if18b070.BusinessLayer.FileHandler;
import at.technikum_wien.if18b070.Models.PhotographerModel;
import at.technikum_wien.if18b070.Models.PictureModel;
import at.technikum_wien.if18b070.PresentationModels.PhotographerViewModel;
import at.technikum_wien.if18b070.PresentationModels.PictureViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.tinylog.Logger;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


public class FXMLController implements Initializable {

    /* Menu Bar */
    @FXML
    public MenuBar topMenuBar;
    @FXML
    public MenuItem ChooseFile;
    @FXML
    public MenuItem getAllPhotographers;
    @FXML
    public MenuItem newPhotographer;

    /* Active Image */
    @FXML
    public AnchorPane imgActiveContainer;
    @FXML
    public ImageView imgActive;

    /* IPTC */
    @FXML
    public TextField iptc_category;
    @FXML
    public TextField iptc_urgency;
    @FXML
    public TextField iptc_city;
    @FXML
    public TextField iptc_headline;
    @FXML
    public Button SaveIPTCButton;

    /* EXIF */
    @FXML
    public TextField exif_fileformat;
    @FXML
    public TextField exif_country;
    @FXML
    public TextField exif_iso;
    @FXML
    public TextField exif_caption;

    /* Photographer */
    public PhotographerViewModel photographerViewModel;
    @FXML
    public TextField name;
    @FXML
    public TextField surname;
    @FXML
    public TextField birthday;
    @FXML
    public TextField country;

    /* ScrollPane */
    @FXML
    public HBox imgScrollPaneHBox;

    /* Random Label for HelloFX thingy*/
    @FXML
    private Label label;



    private PictureViewModel PictureViewModel;
    private List<PictureViewModel> ListofModels = new ArrayList<PictureViewModel>();
    //private List<PictureModel> pictures;


    //*****************************************Initialisation**************************************************************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prep();
        initializeMenuBar();

        loadAllPictures();

        fillScrollPane();
        initializeActivePicture();
        Logger.debug("Successfully Initialized.");

    }

    private void initializeMenuBar(){
        //this.newPhotographer.setOnAction(this::handleNewPhotographer);
        //this.getAllPhotographers.setOnAction(this::handleGetAllPhotographers);
    }

    private void initializeActivePicture() {
        // set activePictureViewModel

        Logger.debug(ListofModels.get(0).path.getValue());
        PictureViewModel = new PictureViewModel(Main.DATABASE.getPictureModelFromPath(ListofModels.get(0).path.getValue()));

        //PictureViewModel = new PictureViewModel(new PictureModel(ListofModels.get(0).path.getValue()));

        updateActiveImage();
        Logger.debug("Successfully set ActiveImage.");
    }

    //**************************************** Version/Preperation ******************************************************

    private void prep(){
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        this.label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");

        /*searchBar.setOnKeyReleased(event -> {
            loadThumbnails();
            updateScrollPane();
        });*/

        SaveIPTCButton.setOnAction(event -> {
            PictureModel pm = PictureViewModel.getPictureModel();

            pm.setCatergory(iptc_category.getText());
            pm.setUrgency(iptc_urgency.getText());
            pm.setCity(iptc_city.getText());
            pm.setHeadline(iptc_headline.getText());

            Main.DATABASE.updateIPTC(pm);

            PictureViewModel.updateProperties();
        });

        this.imgActive.fitWidthProperty().bind(this.imgActiveContainer.widthProperty());
        this.imgActive.fitHeightProperty().bind(this.imgActiveContainer.heightProperty());
        Logger.debug("Successfully Prepared necessary preperations");
    }

    //**************************************** loadAllPictures and Populate Database ******************************************************
    private void loadAllPictures(){

        for(String path: new FileHandler().getAllPaths()){
            ListofModels.add(new PictureViewModel(new PictureModel(path)));
        }
        Logger.debug("Successfully loaded all Pictures.");
    }

    //****************************** Fill Scroll Pane with all Pictures *******************************************
    private void fillScrollPane() {
        imgScrollPaneHBox.getChildren().clear();

        for(PictureViewModel pvm : ListofModels) {

            Image img = new Image("file:" + pvm.path.getValue());
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(200);
            imgView.setPreserveRatio(true);


            imgView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                //set new PictureModel for the active ViewModel
                PictureViewModel.setPictureModel(Main.DATABASE.getPictureModelFromPath(pvm.path.getValue()));
                //PictureViewModel.setPictureModel(new PictureModel(pvm.path.getValue()));
                PictureViewModel.updateProperties();

                updateActiveImage();
                event.consume();
            });
            imgScrollPaneHBox.getChildren().add(imgView);
        }
        Logger.debug("Successfully filled Scrollpane.");
    }

    //*********************** update Active Picture that is displayed *************************************
    private void updateActiveImage(){
        Image image = new Image("file:" + PictureViewModel.path.getValue());
        imgActive.setImage(image);

        iptc_category.setText(PictureViewModel.category.getValue());
        iptc_urgency.setText(PictureViewModel.urgency.getValue());
        iptc_city.setText(PictureViewModel.city.getValue());
        iptc_headline.setText(PictureViewModel.headline.getValue());

        exif_fileformat.setText(PictureViewModel.fileformat.getValue());
        exif_country.setText(PictureViewModel.country.getValue());
        exif_iso.setText(PictureViewModel.iso.getValue());
        exif_caption.setText(PictureViewModel.caption.getValue());
        Logger.debug("Successfully updated ActiveImage.");
    }
}