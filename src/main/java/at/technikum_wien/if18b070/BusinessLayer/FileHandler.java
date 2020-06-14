package at.technikum_wien.if18b070.BusinessLayer;
import at.technikum_wien.if18b070.Main;
import at.technikum_wien.if18b070.PresentationModels.PictureViewModel;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {
    private List<PictureViewModel> ListofModels = new ArrayList<PictureViewModel>();

    // File representing the folder that you select using a FileChooser
    static final File dir = Main.BILDER;

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = (dir, name) -> {
        for (final String ext : EXTENSIONS) {
            if (name.endsWith("." + ext)) {
                return (true);
            }
        }
        return (false);
    };

    public List<String> getAllPaths() {

        ArrayList<String> paths = new ArrayList<>();

        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                paths.add(f.getPath());
            }
        }
        return paths;
    }

    //as reference
    /*public List<PictureModel> getPictureModels() {
        return this.pictures;
    }

    public PictureModel getPictureModelFromPath(String path) {
        // find picture with given path
        for(PictureModel pm : pictures) {
            if(path.equals(pm.getPath())) {
                return pm;
            }
        }
        return null;
    }*/
}
