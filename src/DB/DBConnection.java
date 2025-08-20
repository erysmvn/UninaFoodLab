package DB;

import java.sql.*;

public class DBConnection {
    private Connection con = null;
    private Statement stmt = null;
    private String url = "jdbc:postgresql://aws-0-eu-west-2.pooler.supabase.com:5432/postgres?user=carmine.rcuvzqxbvjqdlpmuovmj&password=password";
    private String user = "carmine.rcuvzqxbvjqdlpmuovmj";
    private String password = "password";

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
