package sample;

import java.sql.*;

public class DB {

    private final String HOST = "localhost";
    private final String PORT = "3307";
    private final String DB_NAME = "shorter_links";
    private final String LOGIN = "root";
    private final String PASS = "root";

    private Connection dbConn = null;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    public boolean getShortLink(String short_link) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM `links` WHERE `short_link` = '" + short_link + "' LIMIT 1";

        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        if (res.next())
            return false;

        return true;
    }


    public void insertlinks(String long_link, String short_link) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO `links` (long_link, short_link) VALUES (?, ?)";

        PreparedStatement prSt = getDbConnection().prepareStatement(sql);
        prSt.setString(1, long_link);
        prSt.setString(2, short_link);

        prSt.executeUpdate();
    }

    public ResultSet getAllLinks() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM `links`";

        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        return res;
    }

    public void deletelink(int id) throws SQLException, ClassNotFoundException {
         String sql = "DELETE FROM `links` WHERE `id` = '" + id + "'";

        Statement statement = getDbConnection().createStatement();
        statement.executeUpdate(sql);
    }


}
