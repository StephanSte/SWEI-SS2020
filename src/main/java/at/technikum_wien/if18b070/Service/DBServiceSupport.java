package at.technikum_wien.if18b070.Service;

import at.technikum_wien.if18b070.Models.PhotographerModel;
import at.technikum_wien.if18b070.Models.PictureModel;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public interface DBServiceSupport {
    public boolean addNewImage(PictureModel model);
    public boolean addIPTC(PictureModel model);
    public boolean addEXIF(PictureModel model);
    public boolean addNewPhotographer(PhotographerModel model);

    public boolean updatePhotographerForImage(PictureModel model);
    public boolean updatePhotographer(PhotographerModel model);
    public boolean updateIPTC(PictureModel model);
    public boolean addPhotographerToPicture(String path, String fhid);

    public PictureModel getPictureModelFromPath(String path);
    //public PhotographerModel getPhotographerForImage(File image);
    public Collection<PhotographerModel> getPhotographers();
    public ArrayList<String> getPathsFromSearchString(String search);
    public PhotographerModel getPhotographerForPicture(String photographerID);
    public PhotographerModel getPhotographerFromFhid(String fhid);
    public ArrayList<String> getPhotographerFhids();
    public void DeleteDatabase()throws SQLException;

    public void close();
}
