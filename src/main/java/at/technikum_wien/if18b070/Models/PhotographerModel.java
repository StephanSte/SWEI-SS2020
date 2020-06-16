package at.technikum_wien.if18b070.Models;

import at.technikum_wien.if18b070.Main;
import javafx.scene.image.Image;

import javax.imageio.plugins.tiff.ExifGPSTagSet;

public class PhotographerModel {
    private String fhid;
    public String name;
    public String birthday;
    public String surname;
    public String country;

    @Override
    public String toString(){
        return "FH-ID:  " + this.fhid + "   Name: " + this.name + "     Surname: " + this.surname + "   Birthday: "+ this. birthday + "     Country: " + this.country;
    }

    public PhotographerModel(){
        this.fhid = "";
        this.name = "";
        this.surname = "";
        this.birthday = "";
        this.country = "";
    }
    public PhotographerModel(String fhid) { this.setFhid(fhid); }

    public String getFhid() { return fhid; }

    public void setFhid(String fhid) { this.fhid = fhid; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}