package Controller;

import GUI.*;
import java.sql.*;
import DB.DBConnection;

import javax.swing.*;

public class Controller {
    DBConnection db;
    Connection con;
    Statement stmt;

    public Controller() {
        db = new DBConnection();
        db.DBConnect();
        con = db.getConnection();

        HomePage homePage = new HomePage(this);
        homePage.setContentPane(homePage.getPanel());
        homePage.setTitle("HomePage");
        homePage.setSize(500, 500);
        homePage.setVisible(true);
        homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void loginStudenteClicked() {
        LoginForm loginForm = new LoginForm(this);
        loginForm.setContentPane(loginForm.getPanel());
        loginForm.setTitle("Login");
        loginForm.setSize(500, 500);
        loginForm.setVisible(true);
        loginForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public boolean validateLoginStudente(String email, String psw) throws Exception{

        String sql = "select COUNT(*) from studente where email = '" + email + "' and passw = md5('" + psw + "');";

        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        rs.next();
        int count = rs.getInt(1);
        return count == 1;
    }
}
