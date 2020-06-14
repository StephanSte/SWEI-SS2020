package at.technikum_wien.if18b070.Service;

import at.technikum_wien.if18b070.Models.PhotographerModel;
import at.technikum_wien.if18b070.Models.PictureModel;

import java.io.File;
import java.util.Collection;

public interface DBServiceSupport {
    public boolean addNewImage(PictureModel model);
    public boolean addNewPhotographer(PhotographerModel model);

    public boolean updatePhotographerForImage(PictureModel model);
    public boolean updatePhotographer(PhotographerModel model);

    public PhotographerModel getPhotographerForImage(File image);
    public Collection<PhotographerModel> getPhotographers();

    public void close();
}
