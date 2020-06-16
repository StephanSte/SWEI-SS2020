package at.technikum_wien.if18b070.BusinessLayer;
import at.technikum_wien.if18b070.Main;
import at.technikum_wien.if18b070.Models.PictureModel;
import at.technikum_wien.if18b070.PresentationModels.PictureViewModel;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.tinylog.Logger;


public class FileHandler {

    private List<PictureViewModel> ListofModels = new ArrayList<PictureViewModel>();
    private PictureModel picture;

    // File representing the folder that you select using a FileChooser
    static final File dir = Main.BILDER;

    //Randomizer
    Random r = new Random();
    private int low = 1;
    private int high = 100;
    private int Number = 0;


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

    public List<String> getAllPaths(){

        ArrayList<String> paths = new ArrayList<>();

        if (dir.isDirectory()) { // make sure it's a directory
            //reset Database
            try {
                Main.DATABASE.DeleteDatabase();
            }catch (SQLException e) {
                Logger.debug("Something went wrong when deleting the database");
                e.printStackTrace();
            }

            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                //add path to list
                paths.add(f.getPath());



                //create New Picturemodel
                picture = new PictureModel(f.getPath());

                //generate Random number so db can get Populated
                this.Number = r.nextInt(high-low) + low;

                picture.setPath(f.getPath());
                picture.setFileformat(getRandomFileFormat());
                picture.setCountry(getRandomCountry());
                picture.setIso(getRandomIso());
                picture.setCaption(getRandomCaption());
                picture.setCatergory(getRandomCategory());
                picture.setUrgency(getRandomUrgency());
                picture.setCity(getRandomCity());
                picture.setHeadline(getRandomHeadline());
                picture.setPhotographerID(null);
                //add Picture to DB
                Main.DATABASE.addNewImage(picture);
            }
        }
        return paths;
    }

    //simple generation of "Random" data

    private String getRandomFileFormat(){
        String result = "";

        if (Number <= 33){
            result = "jpg";
            return result;
        }else if(Number <= 66 && Number > 33){
            result = "png";
            return result;
        }else if(Number > 66){
            result = "gif";
            return result;
        }else{
            result = "somethingWrong";
        }
        return result;
    }
    private String getRandomCountry(){
        String result = "";

        if (Number <= 33){
            result = "Austria";
            return result;
        }else if(Number <= 66 && Number > 33){
            result = "Germany";
            return result;
        }else if(Number > 66){
            result = "Buxdehude";
            return result;
        }else{
            result = "SomethingWrong";
        }
        return result;
    }
    private String getRandomIso(){
        String result = "";

        if (Number <= 33){
            result = "Fuenfzig";
            return result;
        }else if(Number <= 66 && Number > 33){
            result = "Hundert";
            return result;
        }else if(Number > 66){
            result = "Tausend";
            return result;
        }else{
            result = "SomethingWrong";
        }
        return result;

    }
    private String getRandomCaption(){
        String result = "";

        if (Number <= 33){
            result = "Bli";
            return result;
        }else if(Number <= 66 && Number > 33){
            result = "Bla";
            return result;
        }else if(Number > 66){
            result = "Blub";
            return result;
        }else{
            result = "SomethingWrong";
        }
        return result;
    }
    private String getRandomCategory(){
        String result = "";

        if (Number <= 33){
            result = "Blumen";
            return result;
        }else if(Number <= 66 && Number > 33){
            result = "Haus";
            return result;
        }else if(Number > 66){
            result = "Landschaft";
            return result;
        }else{
            result = "SomethingWrong";
        }
        return result;
    }
    private String getRandomUrgency(){
        String result = "";

        if (Number <= 33){
            result = "very";
            return result;
        }else if(Number <= 66 && Number > 33){
            result = "alot";
            return result;
        }else if(Number > 66){
            result = "three";
            return result;
        }else{
            result = "SomethingWrong";
        }
        return result;
    }
    private String getRandomCity(){
        String result = "";

        if (Number <= 33){
            result = "Bregenz";
            return result;
        }else if(Number > 33 && Number <= 66 ){
            result = "Vienna";
            return result;
        }else if(Number > 66){
            result = "Graz";
            return result;
        }else{
            result = "Europe";
        }
        return result;
    }
    private String getRandomHeadline(){
        String result = "";

        if (Number <= 33){
            result = "This is a Headline";
            return result;
        }else if(Number <= 66 && Number > 33){
            result = "This is another headline";
            return result;
        }else if(Number > 66){
            result = "third Headline";
            return result;
        }else{
            result = "SomethingWrong";
        }
        return result;
    }
}
