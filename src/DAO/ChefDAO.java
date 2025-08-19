package DAO;

import Controller.Controller;
import DB.DBConnection;
import Entity.Chef;
import Entity.Corso;

import java.sql.*;
import java.util.ArrayList;

public class ChefDAO {
    DBConnection dbc;
    Statement stmt;
    Connection con;
    Controller controller;

    public ChefDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        this.con = dbc.getConnection();
        this.stmt = dbc.getStatement();
        this.controller = controller;
    }

    public Chef getChefByEmail(String email){
        Chef chef = null;
        String sql = "select * from chef where email = " +  "'" + email + "'";
        try{
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                chef = new Chef(
                        rs.getString("nome_chef"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("passw")
                );
                chef.setCorsi(this.getCorsiFromChef(chef));
            }
        }catch(SQLException sqle){
            System.out.println("Errore nel cercare lo chef dall'email");
        }catch(Exception e){
            e.printStackTrace();
        }

        return chef;
    }

    public Chef getChefByNomeCorso(String nomeCorso) {
        String sql = "SELECT chef.* " +
                "FROM chef " +
                "JOIN tiene ON chef.idchef = tiene.idchef " +
                "JOIN corso ON corso.idcorso = tiene.idcorso " +
                "WHERE corso.nome_corso = ? " +
                "LIMIT 1";

        try  {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, nomeCorso);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Chef chef = new Chef(
                        rs.getString("nome_chef"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("passw")
                );
                chef.setCorsi(getCorsiFromChef(chef));
                return chef;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Chef tryLogin(String sql) throws SQLException{
        Chef chef = null;

        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()){
            chef = new Chef(
                    rs.getString("nome_chef"),
                    rs.getString("cognome"),
                    rs.getString("email"),
                    rs.getString("passw")
            );
            chef.setCorsi(getCorsiFromChef(chef));
        }else{
            SQLException sqlException = new SQLException();
            throw sqlException;
        }

        return chef;
    }

    public Chef tryRegister(Chef chef) throws SQLException{
        String sql = "INSERT INTO chef (nome_chef, cognome, email, passw) VALUES (?, ?, ?, md5(?))";

        try {
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, chef.getNome());
            pstmt.setString(2, chef.getCognome());
            pstmt.setString(3, chef.getEmail());
            pstmt.setString(4, chef.getPassw());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                Exception exc  = new Exception("No row inserted");
                throw exc;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return chef;
}

public ArrayList<Corso> getCorsiFromChef(Chef chef){

    ArrayList<Corso> corsi = new ArrayList<>();
    CorsoDAO corsoDao = new CorsoDAO(controller);

    String sql = "SELECT DISTINCT c.nome_corso " +
            "FROM corso c NATURAL JOIN chef ch NATURAL JOIN tiene " +
            "WHERE ch.email = '" + chef.getEmail() + "'";

    try {
        Statement stmt2 = con.createStatement();
        ResultSet rs2 = stmt2.executeQuery(sql);

        while (rs2.next()) {
            Corso corso = corsoDao.getCorsoByTitle(rs2.getString("nome_corso"));
            corsi.add(corso);
        }

    } catch (SQLException sqle) {
        sqle.printStackTrace();
    }

    return corsi;
}

}