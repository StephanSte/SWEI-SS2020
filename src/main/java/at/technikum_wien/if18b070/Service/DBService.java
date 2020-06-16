package at.technikum_wien.if18b070.Service;

import at.technikum_wien.if18b070.Main;
import at.technikum_wien.if18b070.Models.PhotographerModel;
import at.technikum_wien.if18b070.Models.PictureModel;

import java.sql.*;
import java.util.*;

import org.tinylog.Logger;


public class DBService implements DBServiceSupport{
    private static DBService instance;
    private Connection conn;
    private Dictionary<String, PreparedStatement> preparedStatements = new Hashtable<>();
    
    private static final String CREATE_PHOTOGRAPHER = "create table if not exists  photographer\n" +
            "(\n" +
            "    fhid     text\n" +
            "        constraint photographer_pk\n" +
            "            primary key,\n" +
            "    name     text not null,\n" +
            "    surname  text not null,\n" +
            "    birthday date not null,\n" +
            "    country  text not null\n" +
            ");\n" +
            "\n" +
            "create unique index photographer_fhid_uindex\n" +
            "    on photographer (fhid);";
    private static final String CREATE_PICTURE = "create table if not exists picture\n" +
            "(\n" +
            "    path            text\n" +
            "        constraint picture_pk\n" +
            "            primary key,\n" +
            "    EXIF_fileformat text,\n" +
            "    EXIF_country    text,\n" +
            "    EXIF_iso        text,\n" +
            "    EXIF_caption    text,\n" +
            "    IPTC_category   text,\n" +
            "    IPTC_urgency    text,\n" +
            "    IPTC_city       text,\n" +
            "    IPTC_headline   text,\n" +
            "    photographerID  text\n" +
            "        references photographer\n" +
            ");\n" +
            "\n" +
            "create unique index picture_path_uindex\n" +
            "    on picture (path);";

    private static final String INSERT_IMAGE = "INSERT OR IGNORE INTO picture VALUES(?,?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_IPTC = "INSERT INTO picture(IPTC_CATEGORY, IPTC_URGENCY, IPTC_CITY, IPTC_HEADLINE) VALUES(?,?,?,?)";
    private static final String INSERT_EXIF = "INSERT INTO picture(EXIF_fileformat, EXIF_country, EXIF_ISO, EXIF_CAPTION) VALUES(?,?,?,?)";
    private static final String INSERT_PHOTOGRAPHER = "INSERT INTO photographer VALUES(?,?,?,?,?)";
    private static final String INSERT_DUMMY_PHOTOGRAPHER = "INSERT INTO photographer VALUES('if18b070','Stephan','Steidl','09.02.1998','Austria')";
    //private static final String INSERT_PHOTOGRAPHER_FOR_PICTURE = "INSERT INTO picture(photographerID) VALUES(?) WHERE path = ?";
    private static final String UPDATE_PHOTOGRAPHER_FOR_IMAGE = "UPDATE picture SET photographerID = ? WHERE path = ?";
    private static final String UPDATE_IPTC = "UPDATE picture SET IPTC_CATEGORY = ?, IPTC_URGENCY = ?, IPTC_CITY = ?, IPTC_HEADLINE = ? WHERE path = ?";
    private static final String UPDATE_PHOTOGRAPHER = "UPDATE photographer SET name = ?, surname = ?, birthday = ?, country = ? WHERE fhid = ?";
    //get the Exif Data of the displayed image
    private static final String RETURN_PHOTOGRAPHER_FROM_IMAGE = "SELECT * FROM photographer WHERE id = (RETURN photographer FROM images WHERE path = ?)";
    //get All Photographers
    private static final String RETURN_PHOTOGRAPHERS = "SELECT * FROM photographer";
    // whole picture selection
    private static final String RETURN_PICTURE_BY_PATH = "SELECT * FROM picture WHERE path = ?";
    private static final String GETFHIDS = "SELECT fhid FROM photographer";
    private static final String GET_PHOTOGRAPHER_FROM_FHID = "SELECT * FROM PHOTOGRAPHER WHERE fhid = ?";
    // selection by filename
    private static final String SELECT_PATHS_BY_FILENAME = "SELECT path FROM pictures WHERE path LIKE ?";
    // selection by EXIF
    private static final String SELECT_PATHS_BY_CATEGORY = "SELECT path FROM pictures WHERE iptc_category LIKE ?";
    private static final String SELECT_PATHS_BY_URGENCY = "SELECT path FROM pictures WHERE iptc_urgency LIKE ?";
    private static final String SELECT_PATHS_BY_CITY = "SELECT path FROM pictures WHERE iptc_city LIKE ?";
    private static final String SELECT_PATHS_BY_HEADLINE = "SELECT path FROM pictures WHERE iptc_headline LIKE ?";
    private static final String GET_SIZE_OF_PHOTOGRAPHER_TABLE = "SELECT count(fhid) FROM photographer";

    private static final String DELETE_DATABASE = "DELETE FROM picture";



    public DBService() throws SQLException {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:SWEIDB.db");
            Logger.debug("Successfully established SQLite database connection.");

            //initializeDatabase();
        }catch (SQLException e){
            Logger.debug("Failed to establish SQLite database connection.");
            Logger.trace(e);
        }
        createTables();
    }

    private void createTables() throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.execute(CREATE_PICTURE);
        stmt.execute(CREATE_PHOTOGRAPHER);
        if (getSizeOfPhotograherTable() == 0){
            stmt.execute(INSERT_DUMMY_PHOTOGRAPHER);
        }

        stmt.close();
    }


    public int getSizeOfPhotograherTable(){
        try {
            PreparedStatement statement = conn.prepareStatement(GET_SIZE_OF_PHOTOGRAPHER_TABLE);
            ResultSet rs = statement.executeQuery();

            int Size = 0;
            while (rs.next()) {
                Size = rs.getInt("count(fhid)");
            }

            statement.close();
            return Size;

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.debug("Failed to retrieve size");
            return 5;
        }
    }

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
            statement.setString(1,model.getFhid());
            statement.setString(2,model.getName());
            statement.setString(3,model.getSurname());
            statement.setString(4,model.getBirthday());
            statement.setString(5,model.getCountry());
            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean addPhotographerToPicture(String path, String fhid){
        try{
            PreparedStatement statement = conn.prepareStatement(UPDATE_PHOTOGRAPHER_FOR_IMAGE);
            statement.setString(1,fhid);
            Logger.debug(fhid);
            statement.setString(2,path);
            Logger.debug(path);

            Logger.debug("Set Photographer as Active Picturetaker");
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
            statement.setString(5,model.getFhid());


            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean updateIPTC(PictureModel model){
        try{
            PreparedStatement statement = conn.prepareStatement(UPDATE_IPTC);
            statement.setString(1,model.getCatergory());
            statement.setString(2,model.getUrgency());
            statement.setString(3,model.getCity());
            statement.setString(4,model.getHeadline());
            statement.setString(5,model.getPath());
            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<String> getPhotographerFhids(){
        try{
            PreparedStatement statement = conn.prepareStatement(GETFHIDS);
            ResultSet rs = statement.executeQuery();

            ArrayList<String> result = new ArrayList<>();
            while(rs.next()) result.add(rs.getString("fhid"));
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public PhotographerModel getPhotographerFromFhid(String fhid){
        try{
            PreparedStatement statement = conn.prepareStatement(GET_PHOTOGRAPHER_FROM_FHID);
            statement.setString(1, fhid);
            ResultSet rs = statement.executeQuery();

            PhotographerModel model = new PhotographerModel();

            model.setFhid(fhid);
            model.setName(rs.getString("name"));
            model.setSurname(rs.getString("surname"));
            model.setBirthday(rs.getString("birthday"));
            model.setCountry(rs.getString("Country"));
            //statement.close();
            return model;

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.debug("Failed to retrieve Model from Id");
        }
        return null;
    }
    @Override
    public PhotographerModel getPhotographerForPicture(String photographerID) {
        try{
            PreparedStatement statement = conn.prepareStatement(GET_PHOTOGRAPHER_FROM_FHID);
            ResultSet rs = statement.executeQuery();

            PhotographerModel model = new PhotographerModel();

            model.setFhid(rs.getString("fhid"));
            model.setName(rs.getString("name"));
            model.setSurname(rs.getString("surname"));
            model.setBirthday(rs.getString("birthday"));
            model.setCountry(rs.getString("country"));

            return model;

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.debug("Failed to retrieve Model from Id");
        }
        return null;
    }

    @Override
    public ArrayList<PhotographerModel> getPhotographers() {
        String fhid = null;
        String name = null;
        String surname = null;
        String birthday = null;
        String country = null;
        ArrayList<PhotographerModel> Liste= new ArrayList<>();

        try{
            PreparedStatement stmt = conn.prepareStatement(RETURN_PHOTOGRAPHERS);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                PhotographerModel pm = new PhotographerModel();

                pm.setFhid(rs.getString("fhid"));
                pm.setName(rs.getString("name"));
                pm.setSurname(rs.getString("surname"));
                pm.setBirthday(rs.getString("birthday"));
                pm.setCountry(rs.getString("country"));
                Liste.add(pm);

            }
            Logger.debug("Liste:" + Liste);
            stmt.close();
            return Liste;

        }catch (SQLException e) {
            Logger.debug("Failed to retrieve Photographers.");
            Logger.trace(e);
            return null;
        }
    }
    @Override
    public PictureModel getPictureModelFromPath(String path) {
        try {
            PreparedStatement stmt = conn.prepareStatement(RETURN_PICTURE_BY_PATH);
            stmt.setString(1, path);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                PictureModel pm = new PictureModel(rs.getString("path"));

                pm.setPath(rs.getString("path"));
                pm.setFileformat(rs.getString("EXIF_fileformat"));
                pm.setCountry(rs.getString("EXIF_country"));
                pm.setIso(rs.getString("EXIF_iso"));
                pm.setCaption(rs.getString("EXIF_caption"));
                pm.setCatergory(rs.getString("IPTC_category"));
                pm.setUrgency(rs.getString("IPTC_urgency"));
                pm.setCity(rs.getString("IPTC_city"));
                pm.setHeadline(rs.getString("IPTC_headline"));
                pm.setPhotographerID(rs.getString("photographerID"));

                //Logger.debug("Successfully retrieved picture by path from database.");
                stmt.close();
                return pm;
            }
            else {
                Logger.debug("Failed to retrieve picture by path from database: empty ResultSet returned.");
                return null;
            }

        } catch (SQLException e) {
            Logger.debug("Failed to retrieve picture by path from database.");
            Logger.trace(e);
            return null;
        }
    }
    @Override
    public ArrayList<String> getPathsFromSearchString(String search) {
        try {
            // look for filename
            PreparedStatement stmt1 = conn.prepareStatement(SELECT_PATHS_BY_FILENAME);
            stmt1.setString(1, Main.BILDER + "%" + search + "%");
            // look for category
            PreparedStatement stmt2 = conn.prepareStatement(SELECT_PATHS_BY_CATEGORY);
            stmt2.setString(1, "%" + search + "%");
            // look for urgency
            PreparedStatement stmt3 = conn.prepareStatement(SELECT_PATHS_BY_URGENCY);
            stmt3.setString(1, "%" + search + "%");
            // look for city
            PreparedStatement stmt4 = conn.prepareStatement(SELECT_PATHS_BY_CITY);
            stmt4.setString(1, "%" + search + "%");
            //look for headline
            PreparedStatement stmt5 = conn.prepareStatement(SELECT_PATHS_BY_HEADLINE);
            stmt5.setString(1, "%" + search + "%");

            /* result set containing all paths matching search string in each columns
            HashSet -> no duplicates :) */
            HashSet<String> result = new HashSet<>(Collections.emptySet());

            result.addAll(getPathsFromResultSet(stmt1.executeQuery()));
            stmt1.close();
            result.addAll(getPathsFromResultSet(stmt2.executeQuery()));
            stmt2.close();
            result.addAll(getPathsFromResultSet(stmt3.executeQuery()));
            stmt3.close();
            result.addAll(getPathsFromResultSet(stmt4.executeQuery()));
            stmt4.close();
            result.addAll(getPathsFromResultSet(stmt5.executeQuery()));
            stmt5.close();

            Logger.debug("Successfully retrieved picture paths from database.");
            return new ArrayList<>(result);
        } catch (SQLException e) {
            Logger.debug("Failed to retrieve picture paths from database.");
            Logger.trace(e);
            return null;
        }
    }
    private ArrayList<String> getPathsFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<String> results = new ArrayList<>();
        while(rs.next()) {
            results.add(rs.getString("path"));
        }
        return results;
    }



    @Override
    public void DeleteDatabase() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute(DELETE_DATABASE);
        stmt.close();
    }

    @Override
    public void close() {

    }
}






