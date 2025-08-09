package Controller;

import GUI.*;
import java.sql.*;
import DB.DBConnection;

import javax.swing.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
    DBConnection db;
    Connection con;
    Statement stmt;

    public Controller() {
        db = new DBConnection();
        db.DBConnect();
        con = db.getConnection();
    }

    public void showHomePage(javafx.stage.Stage primaryStage) {
        HomePage homePage = new HomePage(this);

        javafx.scene.Scene scene = new javafx.scene.Scene(homePage, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("HomePage");
        primaryStage.show();
    }

    public void loginStudenteClicked() {
        LoginForm loginForm = new LoginForm(this); // 'this' è il controller
        loginForm.setTitle("Login");
        loginForm.setWidth(500);
        loginForm.setHeight(500);
        loginForm.show();  // mostra la finestra JavaFX
    }

    public void registerNewStudent() {
        RegisterForm regForm = new RegisterForm(this); // 'this' è il controller
        regForm.setTitle("Register");
        regForm.setWidth(500);
        regForm.setHeight(500);
        regForm.show();  // mostra la finestra JavaFX
    }

    public boolean addNewStudent(String matricola, String name, String surname, String email, String password) {
        String sql = "INSERT INTO Studente (matricola, nome_stud, cognome, email, passw) VALUES (?, ?, ?, ?, MD5(?));";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, matricola);
            pstmt.setString(2, name);
            pstmt.setString(3, surname);
            pstmt.setString(4, email);
            pstmt.setString(5, password);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // true se almeno una riga è stata inserita

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
