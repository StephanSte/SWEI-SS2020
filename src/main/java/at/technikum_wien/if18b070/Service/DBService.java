package at.technikum_wien.if18b070.Service;

import at.technikum_wien.if18b070.Models.PhotographerModel;
import at.technikum_wien.if18b070.Models.PictureModel;

import java.io.File;
import java.sql.*;
import java.util.Collection;
import java.util.Dictionary;
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
    private static final String RETURN_PICTURE_BY_PATH = "SELECT * FROM picture WHERE path = ?";

    private static final String getPhotographerByName = "select * from PHOTOGRAPHER where NAME = ? and SURNAME = ?";



    //this may not work
    private static final String getPictureByID = "select * from PICTURE join PHOTOGRAPHER P on PICTURE.PHOTOGRAPHER_ID = P.ID where PICTURE.ID = ?";

    private static final String getIPTCFromPicture = "select IPTC_category, IPTC_urgency, IPTC_city,IPTC_headline from PICTURE where ID = ?";
    private static final String getEXIFFromPicture = "select EXIF_FILEFORMAT, EXIF_COUNTRY, EXIF_ISO, EXIF_CAPTION from PICTURE where ID = ?";

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
    public boolean addIPTC(PictureModel picture){
        try{
            PreparedStatement statement = conn.prepareStatement(INSERT_IPTC);
            statement.setString(1,picture.getCatergory());
            statement.setString(2,picture.getUrgency());
            statement.setString(3,picture.getCity());
            statement.setString(4,picture.getHeadline());

            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean addEXIF(PictureModel picture){
        try{
            PreparedStatement statement = conn.prepareStatement(INSERT_EXIF);
            statement.setString(1,picture.getFileformat());
            statement.setString(2,picture.getCountry());
            statement.setString(3,picture.getIso());
            statement.setString(4,picture.getCaption());

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
            PreparedStatement statement = conn.prepareStatement(UPDATE_PHOTOGRAPHER);
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

    public boolean updateIPTC(PictureModel model){
        try{
            PreparedStatement statement = conn.prepareStatement(UPDATE_IPTC);
            statement.setString(1,model.getCatergory());
            statement.setString(2,model.getUrgency());
            statement.setString(3,model.getCity());
            statement.setString(4,model.getHeadline());
            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /*public PictureModel getIPTCForImage(File image){
        return false;
    }*/


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
        return model;
    }

    private ResultSet queryPreparedStatement(String statement, String argument) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(statement);
        stmt.setString(1, argument);
        return stmt.executeQuery();
    }

    private PhotographerModel getPhotographerModelFromResultSet(ResultSet rs) throws SQLException {
        return new PhotographerModel(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5));
    }

    @Override
    public Collection<PhotographerModel> getPhotographers() {
        return null;
    }

    @Override
    public PictureModel getPictureModelFromPath(String path) {
        try {
            PreparedStatement stmt = conn.prepareStatement(RETURN_PICTURE_BY_PATH);
            stmt.setString(1, path);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                PictureModel pm = new PictureModel(rs.getString("path"));

                pm.setFileformat(rs.getString("fileformat"));
                pm.setCountry(rs.getString("country"));
                pm.setIso(rs.getString("iso"));
                pm.setCaption(rs.getString("caption"));
                pm.setCatergory(rs.getString("category"));
                pm.setUrgency(rs.getString("urgency"));
                pm.setCity(rs.getString("city"));
                pm.setHeadline(rs.getString("headline"));


                //Logger.debug("Successfully retrieved picture by path from database.");
                stmt.close();
                return pm;
            }
            else {
                //Logger.debug("Failed to retrieve picture by path from database: empty ResultSet returned.");
                return null;
            }

        } catch (SQLException e) {
            //Logger.debug("Failed to retrieve picture by path from database.");
            //Logger.trace(e);
            return null;
        }
    }


    @Override
    public void close() {

    }
}






