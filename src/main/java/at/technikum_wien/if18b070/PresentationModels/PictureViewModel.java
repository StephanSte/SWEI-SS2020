package at.technikum_wien.if18b070.PresentationModels;

import at.technikum_wien.if18b070.Models.PictureModel;
import javafx.beans.property.SimpleStringProperty;


public class PictureViewModel {

    private PictureModel picture;
    private SimpleStringProperty path;
    private SimpleStringProperty exif;
    private SimpleStringProperty iptc;

    private PictureViewModel(PictureModel picture){
        
    }
}
