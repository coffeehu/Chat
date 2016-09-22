import java.sql.*;

/**
 * Created by hc on 2016/9/2.
 */
public class DbHelper {
    private Connection conn = null;
    private PreparedStatement statement = null;


    public void connect() {
        String urle = "jdbc:mysql://localhost:3306/chat";
        String username = "root";
        String password = "123";
        try {
            Class.forName("com.mysql.jdbc.Driver" );
            conn = DriverManager.getConnection(urle, username, password );
        }

        catch ( ClassNotFoundException cnfex ) {
            System.err.println(
                    "driver error" );
            cnfex.printStackTrace();
        }

        catch ( SQLException sqlex ) {
            System.err.println( "fail connect db" );
            sqlex.printStackTrace();
        }
    }


    public void disconnect() {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ResultSet select(String sql) {
        ResultSet rs = null;
        try {
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


    public boolean insert(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
