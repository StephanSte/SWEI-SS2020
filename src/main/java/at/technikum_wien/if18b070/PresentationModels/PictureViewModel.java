package at.technikum_wien.if18b070.PresentationModels;

import at.technikum_wien.if18b070.Models.PictureModel;
import javafx.beans.property.SimpleStringProperty;



public class PictureViewModel {

    public PictureModel picture;
    //PATH
    public SimpleStringProperty path;
    //EXIF
    public SimpleStringProperty fileformat;
    public SimpleStringProperty dateCreated;
    public SimpleStringProperty country;
    public SimpleStringProperty iso;
    public SimpleStringProperty caption;
    //IPTC
    public SimpleStringProperty category;
    public SimpleStringProperty urgency;
    public SimpleStringProperty keywords;
    public SimpleStringProperty city;
    public SimpleStringProperty headline;
    //PhotographerID
    public SimpleStringProperty photographerID;


    public PictureViewModel(PictureModel picture){
        this.picture = picture;
        //PATH
        path = new SimpleStringProperty();
        //EXIF
        fileformat = new SimpleStringProperty();
        dateCreated = new SimpleStringProperty();
        country = new SimpleStringProperty();
        iso = new SimpleStringProperty();
        caption = new SimpleStringProperty();
        //IPTC
        category = new SimpleStringProperty();
        urgency = new SimpleStringProperty();
        keywords = new SimpleStringProperty();
        city = new SimpleStringProperty();
        headline = new SimpleStringProperty();
        photographerID = new SimpleStringProperty();

        //update after editing a picture
        updateProperties();
    }

    public PictureModel getPictureModel(){ return picture; }
    public void setPictureModel(PictureModel picture) {
        this.picture = picture;
    }

    //update after editing a picture
    public void updateProperties(){
        //Set new path
        path.set(picture.getPath());
        //set new EXIF value
        fileformat.set(picture.getFileformat());
        country.set(picture.getCountry());
        iso.set(picture.getIso());
        caption.set(picture.getCaption());
        //set new IPTC values
        category.set(picture.getCatergory());
        urgency.set(picture.getUrgency());
        keywords.set(picture.getKeywords());
        city.set(picture.getCity());
        headline.set(picture.getHeadline());
        photographerID.set(picture.getPhotographerID());
    }
}
