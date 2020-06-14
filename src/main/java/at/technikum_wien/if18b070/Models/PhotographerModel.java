package at.technikum_wien.if18b070.Models;

import at.technikum_wien.if18b070.Main;
import javafx.scene.image.Image;

import javax.imageio.plugins.tiff.ExifGPSTagSet;

public class PhotographerModel {
    public Integer id;
    public String name;
    public String birthday;
    public String surname;
    public String country;

    public PhotographerModel(){
        this(0,"","","", "");
    }

    public PhotographerModel(int id, String name, String surname, String birthday, String country) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.country = country;
    }


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