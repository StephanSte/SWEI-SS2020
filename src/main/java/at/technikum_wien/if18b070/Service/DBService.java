package at.technikum_wien.if18b070.Service;

import at.technikum_wien.if18b070.Models.PhotographerModel;
import at.technikum_wien.if18b070.Models.PictureModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.sql.*;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Logger;

public class DBService implements DBServiceSupport{
    private static DBService instance;

    private final Logger logger = Logger.getLogger("DBService");
    private Dictionary<String, PreparedStatement> preparedStatements = new Hashtable<>();

    public static DBService getInstance() {
        return instance;
    }
    
    private static final String CREATE_PHOTOGRAPHER = "CREATE TABLE IF NOT EXISTS photographer (\n"
            + " id integer PRIMARY KEY,\n"
            + " name text NOT NULL,\n"
            + " surname text NOT NULL,\n"
            + " birthday date NOT NULL,\n"
            + " country text NOT NULL"
            + " );";


    private static final String CREATE_PICTURE = "CREATE TABLE IF NOT EXISTS picture (\n"
            + "id integer PRIMARY KEY,\n"
            + "path text NOT NULL,\n"
            + "EXIF_fileformat text ,\n"
            + "EXIF_country text ,\n"
            + "EXIF_iso text ,\n"
            + "EXIF_caption text ,\n"
            + "IPTC_category text ,\n"
            + "IPTC_urgency text, \n"
            + "IPTC_city text, \n"
            + "IPTC_headline text, \n"
            + "photographerID int"
            + ");";

    private static final String INSERT_IMAGE = "INSERT INTO picture VALUES(?,?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_IPTC = "INSERT INTO picture(IPTC_CATEGORY, IPTC_URGENCY, IPTC_CITY, IPTC_HEADLINE) VALUES(?,?,?,?)";
    private static final String INSERT_EXIF = "INSERT INTO picture(EXIF_fileformat, EXIF_country, EXIF_ISO, EXIF_CAPTION) VALUES(?,?,?,?)";
    private static final String INSERT_PHOTOGRAPHER = "INSERT INTO photographer VALUES(?,?,?,?)";

    private static final String UPDATE_PHOTOGRAPHER_FOR_IMAGE = "UPDATE picture SET photographerID = ? WHERE path = ?";
    private static final String UPDATE_IPTC = "UPDATE picture SET IPTC_CATEGORY = ?, IPTC_URGENCY, IPTC_CITY, IPTC_HEADLINE WHERE path = ?";
    private static final String UPDATE_PHOTOGRAPHER = "UPDATE photographer SET name = ?, surname = ?, birthday = ?, country = ? WHERE id = ?";

    private static final String RETURN_EXIF_FROM_IMAGE = "SELECT * FROM picture(EXIF_fileformat, EXIF_country, EXIF_ISO, EXIF_CAPTION) WHERE img_path = ?";
    private static final String RETURN_IPTC_FROM_IMAGE = "SELECT * FROM picture(IPTC_CATEGORY, IPTC_URGENCY, IPTC_CITY, IPTC_HEADLINE) WHERE img_path = ?";
    private static final String RETURN_PHOTOGRAPHER_FROM_IMAGE = "SELECT * FROM photographer WHERE id = (RETURN photographer FROM images WHERE path = ?)";
    private static final String RETURN_PHOTOGRAPHERS = "SELECT * FROM photographer";


    private static final String searchPicture = "select * from PICTURE join PHOTOGRAPHER P on PICTURE.PHOTOGRAPHER_ID = P.ID where lower(path) like ? or lower(IPTC_headline) like ? or lower(P.surname) like ? or lower(P.NAME) like ?";


    //this may not work
    private static final String getPictureByID = "select * from PICTURE join PHOTOGRAPHER P on PICTURE.PHOTOGRAPHER_ID = P.ID where PICTURE.ID = ?";

    private static final String getIPTCFromPicture = "select IPTC_category, IPTC_urgency, IPTC_city,IPTC_headline from PICTURE where ID = ?";
    private static final String getEXIFFromPicture = "select EXIF_FILEFORMAT, EXIF_COUNTRY, EXIF_ISO, EXIF_CAPTION from PICTURE where ID = ?";
    private static final String getPhotographerByName = "select * from PHOTOGRAPHER where NAME = ? and SURNAME = ?";

    private static final String getPictureID = "select ID from PICTURE where path = ?";
    private static final String getPhotographerId = "select ID from PHOTOGRAPHER where NAME = ? and SURNAME = ?";


    private Connection conn;
    DBService(String filename) throws SQLException {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite" + filename);

            initializeDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void initializeDatabase() throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet result = metaData.getTables(null, null, null, new String[]{"TABLE"});
        if(result.next()){

        }else {
            createTables();
        }
    }

    private void createTables() throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.execute(CREATE_PICTURE);
        stmt.execute(CREATE_PHOTOGRAPHER);
        stmt.close();
    }


    //TODO:: might need to plus 1 everything
    @Override
    public boolean addNewImage(PictureModel picture){
        try{
            PreparedStatement statement = conn.prepareStatement(INSERT_IMAGE);
            statement.setString(1,picture.getPath());
            statement.setString(2,picture.getFileformat());
            statement.setString(3,picture.getCountry());
            statement.setString(4,picture.getIso());
            statement.setString(5,picture.getCaption());
            statement.setString(6,picture.getCatergory());
            statement.setString(7,picture.getUrgency());
            statement.setString(8,picture.getCity());
            statement.setString(9,picture.getHeadline());
            statement.setString(10,picture.getPhotographerID());
            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addNewPhotographer(PhotographerModel model) {
        try{
            PreparedStatement statement = conn.prepareStatement(INSERT_PHOTOGRAPHER);
            statement.setString(1,model.getName());
            statement.setString(2,model.getSurname());
            statement.setString(3,model.getBirthday());
            statement.setString(4,model.getCountry());
            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePhotographerForImage(PictureModel picture) {
        try{
            PreparedStatement statement = conn.prepareStatement(UPDATE_PHOTOGRAPHER_FOR_IMAGE);
            statement.setString(1,picture.getPath());
            statement.setString(2,picture.getPhotographerID());
            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePhotographer(PhotographerModel model) {
        try{
            PreparedStatement statement = conn.prepareStatement(INSERT_PHOTOGRAPHER);
            statement.setString(1,model.getName());
            statement.setString(2,model.getSurname());
            statement.setString(3,model.getBirthday());
            statement.setString(4,model.getCountry());
            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PhotographerModel getPhotographerForImage(File image) {
        PhotographerModel model = new PhotographerModel();
        try{
            ResultSet rs = queryPreparedStatement(RETURN_PHOTOGRAPHER_FROM_IMAGE, image.getAbsolutePath());
            if(rs.next()){
                model = getPhotographerModelFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<PhotographerModel> getPhotographers() {
        return null;
    }

    @Override
    public void close() {

    }
}






