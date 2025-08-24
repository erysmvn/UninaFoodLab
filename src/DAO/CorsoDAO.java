package DAO;

import Controller.Controller;
import DAO.Interfaces.CorsoDAOInterface;
import Entity.*;
import DB.DBConnection;
import Entity.Enum.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class CorsoDAO implements CorsoDAOInterface {
    DBConnection dbc;
    Statement stmt;
    ResultSet rs;
    Connection con;
    Controller controller;

    // Constructors
    public CorsoDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        stmt = dbc.getStatement();
        this.controller = controller;
    }


    // Methods
    public ArrayList<Corso> searchCorsiLikeString(String nomeCorso){
        nomeCorso = nomeCorso.toUpperCase();
        String sql = "SELECT * FROM corso WHERE UPPER(nome_corso) LIKE '%" + nomeCorso + "%'";
        ArrayList<Corso> corsi = new ArrayList<>();
        try {

            Statement tempStmt = dbc.getStatement();
            ResultSet rs = tempStmt.executeQuery(sql);
            Corso corso;

            while (rs.next()) {
               corsi.add(createCorsoByResultSet(rs));

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return corsi;
    }

    private String buildPath(String nomeCorso){
        nomeCorso = nomeCorso.replaceAll("\\s+", "");
        String path = "/Media/CoursesImages/" +nomeCorso+".png";
        return path;
    }

    private Sessione createSessioneByResultSet(ResultSet rs) throws SQLException {
        Sessione sessione = null;
        String modalita = rs.getString("modalita");

        LocalDate data = rs.getDate("data").toLocalDate();
        LocalTime ora = rs.getTime("ora").toLocalTime();
        LocalDateTime orario = LocalDateTime.of(data, ora);

        if (modalita.equals("Presenza")) {
            sessione = new SessionePresenza(
                    rs.getString("luogo"),
                    rs.getFloat("durata"),
                    orario
            );
        }else{
            sessione = new SessioneOnline(
                    rs.getString("link_incontro"),
                    rs.getFloat("durata"),
                    orario
            );
        }


        return sessione;
    }


    private Corso createCorsoByResultSet(ResultSet rs)throws SQLException {
        Corso corso = new Corso(
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
        corso.setImagePath("/Media/CoursesImages/" +nomeCorsoPulito+".png");
        corso.setSessioni(this.getSessioniCorso(rs.getString("nome_corso")));

        return  corso;
    }


    // Get methods
    public ArrayList<Corso> getCorsiConPiuStudenti(int numeroCorsi){
        ArrayList<Corso> corsi = new ArrayList<>();
        String sql = "SELECT corso.nome_corso, count(segue.matricola) as NumStudenti " +
                "FROM corso NATURAL JOIN segue NATURAL JOIN studente " +
                "GROUP BY corso.idCorso, corso.nome_corso " +
                "ORDER BY NumStudenti DESC LIMIT " + numeroCorsi;

        try {
            Statement stmtLocal = con.createStatement();
            ResultSet rsLocal = stmtLocal.executeQuery(sql);
            while (rsLocal.next()) {
                Corso corso = getCorsoByTitle(rsLocal.getString("nome_corso"));
                if (corso != null) {
                    corsi.add(corso);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return corsi;
    }

    private ArrayList<Sessione> getSessioniCorso(String nomeCorso)throws SQLException{
        ArrayList<Sessione> sessioni = new ArrayList<>();

        String sql = "SELECT * FROM corso NATURAL JOIN tiene NATURAL JOIN sessione WHERE nome_corso = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, nomeCorso);
        ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessioni.add(createSessioneByResultSet(rs));
            }

        return sessioni;
    }

    @Override
    public Corso getCorsoByTitle(String Title) {

        String sql = "SELECT * FROM corso WHERE nome_corso = " + "'" +Title+ "'";

        Corso corso = null;
        try {
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                corso = createCorsoByResultSet(rs);
            }else{
                System.out.println("Corso non trovato");
            }
        }catch (SQLException sqle){
            System.out.println("Errore nel cercare il corso");
            System.out.println(sqle.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
        return corso;
    }

    public void getRicetteTrattate(Corso corso){
        corso.allocaArrayRicette();
        Ricetta ricetta = null;
        String sql = "SELECT DISTINCT nome_ricetta, descrizione_ricetta, tempo_Di_Preparazione, autore " +
                "FROM corso NATURAL JOIN sessione NATURAL JOIN tratta NATURAL JOIN ricetta " +
                "WHERE idcorso = " + "'" +corso.getIdCorso()+"'";

        try  {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    ricetta = new Ricetta(
                            rs.getString("nome_ricetta"),
                    rs.getString("descrizione_ricetta"),
                    rs.getInt("tempo_Di_Preparazione"),
                    rs.getString("autore")
                    );
                    corso.addRicetta(ricetta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void getChefs(Corso corso){
        corso.allocaArrayChefs();
        Chef chef = null;
        String sql = "SELECT DISTINCT nome_chef, cognome, email, passw " +
                "FROM chef NATURAL JOIN tiene NATURAL JOIN corso " +
                "WHERE idcorso = " + "'" +corso.getIdCorso()+"'";

        try  {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    chef = new Chef(
                            rs.getString("nome_chef"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("passw")
                    );
                    corso.addChef(chef);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
