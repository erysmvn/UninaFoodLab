package DB;

import java.sql.*;

public class DBConnection {
    private Connection con = null;
    private Statement stmt = null;
    private String url = "...";
    private String user = "...";
    private String password = "...";

    public void DBConnect() {
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return stmt;
    }

    public Connection getConnection() {
        return con;
    }
}
