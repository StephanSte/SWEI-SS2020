package at.technikum_wien.if18b070.Service;

import at.technikum_wien.if18b070.Models.PhotographerModel;
import at.technikum_wien.if18b070.Models.PictureModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public interface DBServiceSupport {
    boolean addNewImage(PictureModel model);
    boolean addNewPhotographer(PhotographerModel model);

    boolean updatePhotographer(PhotographerModel model);
    boolean updateIPTC(PictureModel model);
    boolean addPhotographerToPicture(String path, String fhid);


    PictureModel getPictureModelFromPath(String path);
    Collection<PhotographerModel> getPhotographers();
    ArrayList<String> getPathsFromSearchString(String search);
    PhotographerModel getPhotographerFromFhid(String fhid);
    ArrayList<String> getPhotographerFhids();
    void DeleteDatabase()throws SQLException;

    void close();
}
