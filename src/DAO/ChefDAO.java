package DAO;

import Entity.*;
import Controller.Controller;
import DB.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class ChefDAO {
    DBConnection dbc;
    Statement stmt;
    ResultSet rs;
    Connection con;
    Controller controller;

    public ChefDAO(DBConnection dbConnection, Controller controller) {
        this.dbc = dbConnection;
        this.con = dbc.getConnection();
        this.stmt = dbc.getStatement();
        this.controller = controller;
    }

    public Chef getChefByEmail(String email){
        Chef chef = null;
        String sql = "select * from chef where email = " +  "'" + email + "'";
        try{
            rs = stmt.executeQuery(sql);
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

    public Chef getChefByNomeCorso(String nomeCorso){
        Chef chef = null;
        String sql = "select * from chef natural join corso c natural join tiene where c.nome_corso = " +  "'" + nomeCorso + "' LIMIT 1";
        try{
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                chef = new Chef(
                        rs.getString("nome_chef"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("passw")
                );
                chef.setCorsi(getCorsiFromChef(chef));
            }
        }catch(SQLException sqle){
            System.out.println("Errore nel cercare lo chef dall'email");
        }catch(Exception e){
            e.printStackTrace();
        }


        return chef;
    }

    public Chef tryLogin(String sql) throws SQLException{
        rs = stmt.executeQuery(sql);
        Chef chef = null;
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

    public ArrayList<Corso> getCorsiFromChef(Chef chef){

        ArrayList<Corso> corsi = new ArrayList<>();
        CorsoDAO corsoDao = new CorsoDAO(dbc,controller);

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
