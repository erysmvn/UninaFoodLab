package DAO;

import Controller.Controller;
import DAO.Interfaces.SessioneDAOInterface;
import DB.DBConnection;
import Entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class SessioneDAO implements SessioneDAOInterface {
    DBConnection dbc;
    Connection con;

    public SessioneDAO(DBConnection dbc) {
        this.dbc = dbc;
        con = dbc.getConnection();
    }

    @Override
    public void insertSessione(Sessione sessione) throws SQLException {
        String sql = "";
        String linkOrLuogo="";

        if (sessione instanceof SessioneOnline sessioneOnline) {
           sql = "INSERT INTO sessione (data, ora, modalita, link_incontro, idcorso, durata) VALUES (?, ?, 'Online', ?, ?, ?) RETURNING idsessione";
           linkOrLuogo = sessioneOnline.getLinkIncontro();
        } else if (sessione instanceof SessionePresenza sessionePresenza) {
            sql = "INSERT INTO sessione (data, ora, modalita, luogo, idcorso, durata) VALUES (?, ?, 'Presenza', ?, ?, ?)  RETURNING idsessione";
            linkOrLuogo = sessionePresenza.getLuogo();
        }

        PreparedStatement pstmt = con.prepareStatement(sql);

        pstmt.setObject(1,sessione.getData());
        pstmt.setObject(2,sessione.getOra());
        pstmt.setString(3,linkOrLuogo);
        pstmt.setInt(4,sessione.getCorso().getIdCorso());
        pstmt.setFloat(5,sessione.getDurata());

        ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("idsessione");
                sessione.setIdsessione(id);
            } else
                throw new SQLException("Creating corso failed, no ID obtained.");

    }

    @Override
    public void removeRicetta(Ricetta ricetta, Sessione sessione) throws SQLException {
        String sql = "DELETE FROM tratta WHERE idricetta = ? AND idsessione = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1,ricetta.getIdRicetta());
        pstmt.setInt(2,sessione.getIdSessione());
        int rowDeleted = pstmt.executeUpdate();

        if(rowDeleted <=0)
            throw new SQLException();
    }

    @Override
    public void insertRicettaToSessione(Ricetta ricetta, Sessione sessione)throws SQLException{
            String sql = "INSERT INTO Tratta (idricetta, idsessione) VALUES (?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ricetta.getIdRicetta());
            ps.setInt(2,sessione.getIdSessione());
            ps.executeUpdate();

            RicettaDAO ricettaDAO = new RicettaDAO(dbc);
            sessione.setRicette(ricettaDAO.getRicetteByIdSessione(sessione.getIdSessione()));
    }

    @Override
    public Sessione createSessioneByResultSet(ResultSet rs, Corso corso) throws SQLException {
        Sessione sessione;

        String modalita = rs.getString("modalita");
        LocalDate data = rs.getDate("data").toLocalDate();
        LocalTime ora = rs.getTime("ora").toLocalTime();
        LocalDateTime orario = LocalDateTime.of(data, ora);

        if (modalita.equals("Presenza")) {
            sessione = new SessionePresenza(
                    rs.getInt("idsessione"),
                    rs.getDate("data").toLocalDate(),
                    rs.getString("luogo"),
                    rs.getFloat("durata"),
                    orario,
                    corso
            );
            FoglioAdesioneDAO foglio = new FoglioAdesioneDAO(dbc);
            ((SessionePresenza)sessione).setFogliAdesione(foglio.getFogliAdesioneByIdSessione(rs.getInt("idsessione")));
        } else {
            sessione = new SessioneOnline(
                    rs.getInt("idsessione"),
                    rs.getDate("data").toLocalDate(),
                    rs.getString("link_incontro"),
                    rs.getFloat("durata"),
                    orario,
                    corso
            );
        }

        RicettaDAO ricettaDAO = new RicettaDAO(dbc);
        sessione.setRicette(ricettaDAO.getRicetteByIdSessione(rs.getInt("idsessione")));

        return sessione;
    }

    @Override
    public ArrayList<Sessione> getSessioniByCorso(Corso corso) throws SQLException {

        ArrayList<Sessione> sessioni = new ArrayList<>();
        String sql = "SELECT * FROM sessione WHERE idcorso = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, corso.getIdCorso());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            sessioni.add(createSessioneByResultSet(rs,corso));
        }

        return sessioni;
    }

    @Override
    public void update(Sessione sessione) throws SQLException {
       String sql="";
       String linkOrLuogo="";
        if (sessione instanceof SessionePresenza sp) {
            sql = "UPDATE sessione " +
                    "SET data = ?, ora = ?, durata = ?, luogo = ? "+
                    "WHERE idsessione = ?";
            linkOrLuogo = sp.getLuogo();
        } else if (sessione instanceof SessioneOnline so) {
            sql = "UPDATE sessione " +
                    "SET data = ?, ora = ?, durata = ?, link_incontro = ? " +
                    "WHERE idsessione = ?";
            linkOrLuogo = so.getLinkIncontro();
        }

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setObject(1,sessione.getData());
        pstmt.setObject(2,sessione.getOra());
        pstmt.setFloat(3,sessione.getDurata());
        pstmt.setString(4,linkOrLuogo);
        pstmt.setInt(5,sessione.getIdSessione());

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected == 0)
            throw new SQLException("Nessuna sessione aggiornata. ID non trovato: " + sessione.getIdSessione());

    }

    @Override
    public void delete(Sessione sessione) throws SQLException {
        String sql = "DELETE FROM sessione WHERE idsessione = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1,sessione.getIdSessione());
        int rowsAffected = pstmt.executeUpdate();
        
        if (rowsAffected < 1) {
            throw new SQLException("Nessuna sessione aggiornata.");
        }
    }
}
