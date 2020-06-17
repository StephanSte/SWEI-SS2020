package at.technikum_wien.if18b070;

import at.technikum_wien.if18b070.BusinessLayer.FileHandler;
import at.technikum_wien.if18b070.Models.PhotographerModel;
import at.technikum_wien.if18b070.Models.PictureModel;
import at.technikum_wien.if18b070.PresentationModels.PhotographerViewModel;
import at.technikum_wien.if18b070.PresentationModels.PictureViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class FXMLController implements Initializable {

    /* Menu Bar */
    @FXML
    public TextField searchBar;

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
    @FXML
    public Label NewPhotographerWrongValues;

    /* All Photographers */
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
    @FXML
    public Label PhotographerWrongValues;

    /* ScrollPane */
    @FXML
    public HBox imgScrollPaneHBox;


    private ObservableList<PhotographerModel> ObservablePhotographerModel = FXCollections.observableArrayList();
    private ListView<PhotographerModel> ListViewPhotographerModel= new ListView<>();

    private PictureViewModel PictureViewModel;
    private PhotographerViewModel PhotographerViewModel;
    private List<PictureViewModel> ListofModels = new ArrayList<>();
    private ArrayList<String> photographerFhids = new ArrayList<>();

    /**
     * inizialisiert Prep Funktion (inizialisiert SaveButtons und Search Bar), lädt alle Bilder, PhotographerIds, stellt
     * Photographen dar und Inizialisert activen Photographen.
     * @param url Die Url
     * @param rb benötigtes Resource Budle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prep();
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


    private void initializeActivePicture() {
        // set activePictureViewModel

        //Logger.debug(ListofModels.get(0).path.getValue());
        PictureViewModel = new PictureViewModel(Main.DATABASE.getPictureModelFromPath(ListofModels.get(0).path.getValue()));

        //PictureViewModel = new PictureViewModel(new PictureModel(ListofModels.get(0).path.getValue()));

        updateActiveImage();
        Logger.debug("Successfully set ActiveImage.");
    }

    //**************************************** Version/Preperation ******************************************************
    //**************************************** Set and Update IPTC and Insert new Photographer ******************************************************

    private void prep(){
        FileHandler fileHandler = new FileHandler();
        fileHandler.setAllPaths();
        searchBar.setOnKeyReleased(event -> {
            loadAllPictures();
            fillScrollPane();
        });

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
            ArrayList<String> Fhids;
            //photographer.setFhid(newfhid.getText());

            String Errormessage = "";
            Fhids = Main.DATABASE.getPhotographerFhids();
            Logger.debug(Fhids);
            if(newfhid.getText().isEmpty() || Fhids.contains(newfhid.getText())){
                Errormessage += "Fhid is empty or already exists";
            }else {
                photographer.setFhid(newfhid.getText());
            }

            if(newname.getText().length() > 100){
                Errormessage += "Length of name invalid";
            }else{
                photographer.setName(newname.getText());
            }

            if (newsurname.getText().isEmpty() || newsurname.getText().length() > 50){
                Errormessage +=  "\n Surname must be set and length can`t be longer than 50 characters";
            }else{
                photographer.setSurname(newsurname.getText());
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDateTime now = LocalDateTime.now();
            try {
                String date = newbirthday.getText();
                Date givenDate= new SimpleDateFormat("dd.MM.yyyy").parse(date);
                Date localDate= java.sql.Timestamp.valueOf(now);
                if (givenDate.after(localDate)){
                    Errormessage += "\n Given Date is in the Future";
                }else{
                    photographer.setBirthday(newbirthday.getText());
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                Logger.debug("Failed to parse given date in handlePhotographerInfo");
            }

            if (Errormessage.isEmpty()){
                photographer.setCountry(newcountry.getText());
                NewPhotographerWrongValues.setText("Everything is alright!");
                Main.DATABASE.addNewPhotographer(photographer);

                PhotographerViewModel pvm = new PhotographerViewModel(photographer);
                pvm.updatePhotographerProperties();
                displayPhotographers();
            }else{
                NewPhotographerWrongValues.setText(Errormessage);
            }
            event.consume();
        });

        this.imgActive.fitWidthProperty().bind(this.imgActiveContainer.widthProperty());
        this.imgActive.fitHeightProperty().bind(this.imgActiveContainer.heightProperty());
        Logger.debug("Successfully Prepared necessary preperations");
    }

    //**************************************** loadAllPictures and Populate Database ******************************************************
    private void loadAllPictures(){
        ListofModels.clear();
        ArrayList<String> Liste;
        if (!searchBar.getText().isEmpty()){
            Liste = Main.DATABASE.getPathsFromSearchString(searchBar.getText());

            Logger.debug("Das ist die Liste: ", Liste);
            for(String path: Liste){
                ListofModels.add(new PictureViewModel(new PictureModel(path)));
            }
        }else {
            for(String path: new FileHandler().getAllPaths()){
                ListofModels.add(new PictureViewModel(new PictureModel(path)));
            }
        }
        Logger.debug("Successfully loaded all Pictures.");
    }

    //**************************************** Display the Photographers in the DB and on the Pane ******************************************************
    private void loadPhotograherFhids(){
        photographerFhids.clear();
        photographerFhids.addAll(Main.DATABASE.getPhotographerFhids());
    }

    private void displayPhotographers(){

        ListViewPhotographerModel = new ListView<>();
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
    //setzt ersten Photographen aus der Tabelle und macht sie Bidirektional
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
    //handle Named Buttons
    private void handleClickedPhotographerButton(Event e) {
        // get fhid from button id
        String fhid = ((Button)e.getSource()).getId();
        // set new active PhotographerModel
        PhotographerViewModel.setPhotographer(
                // get PhotographerModel from database by fhid
                Main.DATABASE.getPhotographerFromFhid(
                        fhid
                )
        );

        // update properties
        PhotographerViewModel.updatePhotographerProperties();
        e.consume();
        Logger.debug("Handeled the Upph click");
    }

    //Speichert neue/geänderte Photographen Daten in Datenbank
    private void handleSavePhotographerInfo(Event e) {
        // PhotographerModel to be updated
        PhotographerModel phm = PhotographerViewModel.getPhotographerModel();
        // set new info

        String Errormessage = "";

        if(name.getText().length() > 100){
            Errormessage += "Length of name invalid";
        }else{
            phm.setName(name.getText());
        }

        if (surname.getText().isEmpty() || surname.getText().length() > 50){
            Errormessage +=  "\n Surname must be set and length can`t be longer than 50 characters";
        }else{
            phm.setSurname(surname.getText());
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        try {
            String date = birthday.getText();
            Date givenDate= new SimpleDateFormat("dd.MM.yyyy").parse(date);
            Date localDate= java.sql.Timestamp.valueOf(now);
            if (givenDate.after(localDate)){
                Errormessage += "\n Given Date is in the Future";
            }else{
                phm.setBirthday(birthday.getText());
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            Logger.debug("Failed to parse given date in handlePhotographerInfo");
        }

        if (Errormessage.isEmpty()){
            phm.setCountry(country.getText());
            PhotographerWrongValues.setText("Everything is alright!");
            Main.DATABASE.updatePhotographer(phm);
            displayPhotographers();
        }else{
            PhotographerWrongValues.setText(Errormessage);
        }
        e.consume();
        Logger.debug("Handeled the other Button click");
    }

    //nimmt aktiven Path und fhid und fügt fhid zu picture als photographerID hinzu
    private void setPictureToPhotographer(Event e){
        String activePicturePath = PictureViewModel.path.getValue();
        Logger.debug(activePicturePath);
        String activePhotographerFhid = PhotographerViewModel.fhid.getValue();
        Logger.debug(activePhotographerFhid);

        Main.DATABASE.addPhotographerToPicture(activePicturePath, activePhotographerFhid);
        PictureViewModel.setPictureModel(Main.DATABASE.getPictureModelFromPath(PictureViewModel.path.getValue()));
        PictureViewModel.updateProperties();
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

        PhotographerViewModel.setPhotographer(Main.DATABASE.getPhotographerFromFhid(PictureViewModel.photographerID.getValue()));

        PhotographerViewModel.updatePhotographerProperties();

        currentPicturefhid.setText(PhotographerViewModel.fhid.getValue());
        currentPicturename.setText(PhotographerViewModel.name.getValue());
        currentPicturesurname.setText(PhotographerViewModel.surname.getValue());
        currentPicturebirthday.setText(PhotographerViewModel.birthday.getValue());
        currentPicturecountry.setText(PhotographerViewModel.country.getValue());
    }
}