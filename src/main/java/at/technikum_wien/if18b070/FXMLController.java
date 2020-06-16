package at.technikum_wien.if18b070;

import at.technikum_wien.if18b070.BusinessLayer.FileHandler;
import at.technikum_wien.if18b070.Models.PhotographerModel;
import at.technikum_wien.if18b070.Models.PictureModel;
import at.technikum_wien.if18b070.PresentationModels.PhotographerViewModel;
import at.technikum_wien.if18b070.PresentationModels.PictureViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    public Text currentPicturefhid;
    @FXML
    public Text currentPicturename;
    @FXML
    public Text currentPicturesurname;
    @FXML
    public Text currentPicturebirthday;
    @FXML
    public Text currentPicturecountry;
    @FXML
    public Button  UpdatePhotographerButton;

    /* New Photographer */
    @FXML
    public TextField newfhid;
    @FXML
    public TextField newname;
    @FXML
    public TextField newsurname;
    @FXML
    public TextField newbirthday;
    @FXML
    public TextField newcountry;
    @FXML
    public Button SaveNewPhotographerButton;

    /* All Photographers */
     @FXML
     public Button updateInfo;
     @FXML
     public Tab preparePhotographer;
     @FXML
     public VBox photographersScrollPaneVBox;
     @FXML
     public Text fhid;
     @FXML
     public TextField name;
     @FXML
     public TextField surname;
     @FXML
     public TextField birthday;
     @FXML
     public TextField country;
    @FXML
    public Button savePhotographerInfo;
    @FXML
    public Button SetPhotographerForActivePicture;


    /* ScrollPane */
    @FXML
    public HBox imgScrollPaneHBox;


    /* Random Label for HelloFX thingy*/
    @FXML
    private Label label;

    private ObservableList<PhotographerModel> ObservablePhotographerModel = FXCollections.observableArrayList();
    private ListView<PhotographerModel> ListViewPhotographerModel= new ListView<>();

    private PictureViewModel PictureViewModel;
    private PhotographerViewModel PhotographerViewModel;
    private PhotographerModel Photographermodel;
    private List<PictureViewModel> ListofModels = new ArrayList<PictureViewModel>();
    private List<PhotographerViewModel> ListOfPhotographers;
    private ArrayList<String> photographerFhids = new ArrayList<>();

    //*****************************************Initialisation**************************************************************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prep();
        initializeMenuBar();
        loadAllPictures();
        //for nothing atm

        //for Photographers
        savePhotographerInfo.setOnAction(this::handleSavePhotographerInfo);
        SetPhotographerForActivePicture.setOnAction(this::setPictureToPhotographer);
        loadPhotograherFhids();
        displayPhotographers();
        initializeActivePhotographer();

        //for Images

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
    //**************************************** Set and Update IPTC and Insert new Photographer ******************************************************

    private void prep(){
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        //this.label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");

        /*searchBar.setOnKeyReleased(event -> {
            loadAllPictures();
            fillScrollPane();
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

        SaveNewPhotographerButton.setOnAction(event -> {
            PhotographerModel photographer = new PhotographerModel();

            photographer.setFhid(newfhid.getText());
            photographer.setName(newname.getText());
            photographer.setSurname(newsurname.getText());
            photographer.setBirthday(newbirthday.getText());
            photographer.setCountry(newcountry.getText());

            Main.DATABASE.addNewPhotographer(photographer);

            PhotographerViewModel pvm = new PhotographerViewModel(photographer);
            pvm.updatePhotographerProperties();
            displayPhotographers();
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
    //**************************************** Display the Photographers in the DB and on the Pane ******************************************************
    private void loadPhotograherFhids(){
        photographerFhids.clear();
        photographerFhids.addAll(Main.DATABASE.getPhotographerFhids());
    }

    private void displayPhotographers(){

        ListViewPhotographerModel = new ListView<PhotographerModel>();
        ObservablePhotographerModel = FXCollections.observableList(Main.DATABASE.getPhotographers());
        photographersScrollPaneVBox.getChildren().clear();

        for(PhotographerModel photographerModel : ObservablePhotographerModel){
            Button button = new Button();

            button.setId(photographerModel.getFhid());
            button.setText(photographerModel.getName() + " " + photographerModel.getSurname());
            button.setOnAction(this::handleClickedPhotographerButton);
            photographersScrollPaneVBox.getChildren().add(button);
        }
    }

    private void initializeActivePhotographer(){
        PhotographerViewModel = new PhotographerViewModel(
                Main.DATABASE.getPhotographerFromFhid(
                        photographerFhids.get(0)
                )
        );
        fhid.textProperty().bindBidirectional(PhotographerViewModel.fhid);
        name.textProperty().bindBidirectional(PhotographerViewModel.name);
        surname.textProperty().bindBidirectional(PhotographerViewModel.surname);
        birthday.textProperty().bindBidirectional(PhotographerViewModel.birthday);
        country.textProperty().bindBidirectional(PhotographerViewModel.country);

    }

    private void handleClickedPhotographerButton(Event e) {
        // get fhid from button id
        String fhid = ((Button)e.getSource()).getId();
        // set new active PhotographerModel
        PhotographerViewModel.setPhotographer(
                // get PhotographerModel from database by email
                Main.DATABASE.getPhotographerFromFhid(
                        fhid
                )
        );

        // update properties
        PhotographerViewModel.updatePhotographerProperties();
        e.consume();
        Logger.debug("Handeled the Upph click");
    }


    private void handleSavePhotographerInfo(Event e) {
        // PhotographerModel to be updated
        PhotographerModel phm = PhotographerViewModel.getPhotographerModel();
        // set new info
        phm.setName(name.getText());
        phm.setSurname(surname.getText());
        phm.setBirthday(birthday.getText());
        phm.setCountry(country.getText());

        Main.DATABASE.updatePhotographer(phm);
        displayPhotographers();
        e.consume();
        Logger.debug("Handeled the other Button click");
    }

    private void setPictureToPhotographer(Event e){
        String activePicturePath = PictureViewModel.path.getValue();
        Logger.debug(activePicturePath);
        String activePhotographerFhid = PhotographerViewModel.fhid.getValue();
        Logger.debug(activePhotographerFhid);

        Main.DATABASE.addPhotographerToPicture(activePicturePath, activePhotographerFhid);
        setPhotographerToImage();
        e.consume();
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

        //fhid.setText(Main.DATABASE.getPhotographerForImage(PictureViewModel.));

        iptc_category.setText(PictureViewModel.category.getValue());
        iptc_urgency.setText(PictureViewModel.urgency.getValue());
        iptc_city.setText(PictureViewModel.city.getValue());
        iptc_headline.setText(PictureViewModel.headline.getValue());

        exif_fileformat.setText(PictureViewModel.fileformat.getValue());
        exif_country.setText(PictureViewModel.country.getValue());
        exif_iso.setText(PictureViewModel.iso.getValue());
        exif_caption.setText(PictureViewModel.caption.getValue());

        if (PictureViewModel.photographerID.getValue() != null) {
            setPhotographerToImage();
        }else{
            currentPicturefhid.setText("");
            currentPicturename.setText("");
            currentPicturesurname.setText("");
            currentPicturebirthday.setText("");
            currentPicturecountry.setText("");
        }

        Logger.debug("Successfully updated ActiveImage.");
    }

    private void setPhotographerToImage(){
        Photographermodel = Main.DATABASE.getPhotographerForPicture(PictureViewModel.photographerID.getValue());
        PhotographerViewModel.updatePhotographerProperties();

        currentPicturefhid.setText(PhotographerViewModel.fhid.getValue());
        currentPicturename.setText(PhotographerViewModel.name.getValue());
        currentPicturesurname.setText(PhotographerViewModel.surname.getValue());
        currentPicturebirthday.setText(PhotographerViewModel.birthday.getValue());
        currentPicturecountry.setText(PhotographerViewModel.country.getValue());
    }
}