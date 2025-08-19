package DAO;

import Controller.Controller;
import DAO.Interfaces.CorsoDAOInterface;
import Entity.Corso;
import DB.DBConnection;
import Entity.Enum.*;

import java.sql.*;
import java.util.ArrayList;

public class CorsoDAO implements CorsoDAOInterface {
    DBConnection dbc;
    Statement stmt;
    ResultSet rs;
    Connection con;
    Controller controller;

    public CorsoDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        stmt = dbc.getStatement();
        this.controller = controller;
    }

    @Override
    public Corso getCorsoByTitle(String Title) {

        String sql = "SELECT * FROM corso WHERE nome_corso = " + "'" +Title+ "'";

        Corso corso = null;
        try {
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                System.out.println(rs.getString("nome_corso"));
                corso = new Corso(
                        rs.getInt("idcorso"),
                        rs.getString("nome_corso"),
                        rs.getInt("numerosessioni"),
                        rs.getFloat("ore_totali"),
                        rs.getInt("frequenza_settimanale"),
                        rs.getDate("datainizio"),
                        rs.getDate("datafine"),
                        rs.getFloat("costo"),
                        ModalitaCorso.getFromString( rs.getString("modcorso") ),
                        Difficolta.valueOf(rs.getString("difficolta") ),
                        rs.getString("desc_corso")
                );
                String nomeCorsoPulito = rs.getString("nome_corso").replaceAll("\\s+", "");
                corso.setImagePath("/Images/"+nomeCorsoPulito+".png");
            }else{
                System.out.println("Corso non trovato");
                System.out.println(sql);
            }
        }catch (SQLException sqle){
            System.out.println("Errore nel cercare il corso");
            System.out.println(sqle.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
        return corso;
    }

}
