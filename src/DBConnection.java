import java.sql.*;

public class DBConnection {
    private Connection con = null;
    private Statement stmt = null;
    private String url = "jdbc:postgresql://aws-0-eu-west-2.pooler.supabase.com:5432/postgres?user=francesco.rcuvzqxbvjqdlpmuovmj&password=admin";
    private String user = "francesco";
    private String password = "admin";

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
