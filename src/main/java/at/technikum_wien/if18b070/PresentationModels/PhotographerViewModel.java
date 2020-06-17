package at.technikum_wien.if18b070.PresentationModels;

import at.technikum_wien.if18b070.Models.PhotographerModel;
import javafx.beans.property.SimpleStringProperty;

/**
 * Das PhotographerViewModel beinhaltet alle Simple _ Properties von einem PhotographerModel und hat get und set beutzerrechte auf das
 * darunterliegende PhotographerModel
 */
public class PhotographerViewModel {
    private PhotographerModel Photographer;
    //
    public SimpleStringProperty fhid;
    public SimpleStringProperty name;
    public SimpleStringProperty surname;
    public SimpleStringProperty birthday;
    public SimpleStringProperty country;

    public PhotographerViewModel(PhotographerModel PhotographerModel){
        this.Photographer = PhotographerModel;
        //values
        fhid = new SimpleStringProperty();
        name = new SimpleStringProperty();
        surname = new SimpleStringProperty();
        birthday = new SimpleStringProperty();
        country = new SimpleStringProperty();

        updatePhotographerProperties();
    }

    public PhotographerModel getPhotographerModel(){ return Photographer; }
    public void setPhotographer(PhotographerModel Photographer) { this.Photographer = Photographer; }

    //update after editing a picture
    public void updatePhotographerProperties(){
        fhid.set(Photographer.getFhid());
        name.set(Photographer.getName());
        surname.set(Photographer.getSurname());
        birthday.set(Photographer.getBirthday());
        country.set(Photographer.getCountry());
    }
}
