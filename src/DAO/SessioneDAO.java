package DAO;

import Controller.Controller;
import DAO.Interfaces.SessioneDAOInterface;
import DB.DBConnection;
import Entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class SessioneDAO implements SessioneDAOInterface {
    Controller controller;

    DBConnection dbc;
    Connection con;

    public SessioneDAO(Controller controller){
        this.controller = controller;
        dbc = controller.getDBConnection();
        con = dbc.getConnection();
    }

    @Override
    public Sessione createSessioneByResultSet(ResultSet rs) throws SQLException {
        Sessione sessione = null;
        String modalita = rs.getString("modalita");

        LocalDate data = rs.getDate("data").toLocalDate();
        LocalTime ora = rs.getTime("ora").toLocalTime();
        LocalDateTime orario = LocalDateTime.of(data, ora);

        //TODO SOLO CON IL CONTROLLER
        RicettaDAO ricettaDAO = new RicettaDAO(controller);
        Corso corso = controller.getCorsoDAO().getCorsoByResultSetWithOutSessioni(rs);
        if (modalita.equals("Presenza")) {
            sessione = new SessionePresenza(
                    rs.getInt("idsessione"),
                    rs.getDate("data").toLocalDate(),
                    rs.getString("luogo"),
                    rs.getFloat("durata"),
                    orario,
                    corso
            );
            ((SessionePresenza)sessione).setFogliAdesione(getFogliAdesioneByIdSessione(rs.getInt("idsessione")));
        }else{
            sessione = new SessioneOnline(
                    rs.getInt("idsessione"),
                    rs.getDate("data").toLocalDate(),
                    rs.getString("link_incontro"),
                    rs.getFloat("durata"),
                    orario,
                    corso
            );
        }

        sessione.setRicette(ricettaDAO.getRicetteByIdSessione(rs.getInt("idsessione")));

        return sessione;
    }

    @Override
    public ArrayList<Sessione> getSessioniByNomeCorso(String nomeCorso) throws SQLException {

        ArrayList<Sessione> sessioni = new ArrayList<>();
        String sql = "SELECT * FROM corso NATURAL JOIN tiene natural join sessione WHERE nome_corso = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, nomeCorso);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            sessioni.add(createSessioneByResultSet(rs));
        }

        return sessioni;

    }

    private ArrayList<FoglioAdesione> getFogliAdesioneByIdSessione(int idsessione){
        //todo tramite il controller
        FoglioAdesioneDAO faDAO = new FoglioAdesioneDAO(controller);
        return faDAO.getFogliAdesioneByIdSessione(idsessione);
    }
}
