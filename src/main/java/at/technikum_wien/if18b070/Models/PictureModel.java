package at.technikum_wien.if18b070.Models;

public class PictureModel {
    //picture
    private String path;
    private int photographerID;

    public PictureModel(String path) { this.setPath(path); }
    //path
    public String getPath() { return path; }
    public void setPath(String path) {this.path = path; }
    //photographer id fk
    public int getPhotographerID() { return photographerID; }
    public void setPhotographerID(int photographerID) { this.photographerID = photographerID; }

    //IPTC
    private String category;
    private String urgency;
    private String keywords;
    private String city;
    private String headline;

    public String getCatergory() {
        return category;
    }

    public void setCatergory(String category) {
        this.category = category;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }



    //EXIF
    private String fileformat;
    private String country;
    private String iso;
    private String caption;

    public String getFileformat() {
        return fileformat;
    }

    public void setFileformat(String fileformat) {
        this.fileformat = fileformat;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }



}
