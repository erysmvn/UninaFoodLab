import Controller.Controller;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {

        Controller controller = new Controller();

        DBConnection db = new DBConnection();
        db.DBConnect();
        Connection con = db.getConnection();

        String sql = "select nome_chef from chef where cognome = 'Scicchitano';";

        Statement stmt = db.getStatement();
        ResultSet rs = stmt.executeQuery(sql);

        rs.next();
        String toret = rs.getString(1);
        System.out.println(toret);

        con.close();
    }
}