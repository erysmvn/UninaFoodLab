package DAO;

import Controller.Controller;
import DAO.Interfaces.CorsoDAOInterface;
import Entity.*;
import DB.DBConnection;
import Entity.Enum.*;
import Exception.CorsoExceptions.corsiNotFoundException;

import java.math.BigDecimal;
import java.sql.*;

import java.util.ArrayList;

public class CorsoDAO implements CorsoDAOInterface {
    DBConnection dbc;

    Connection con;
    Controller controller;

    // Constructors
    public CorsoDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        this.controller = controller;
    }

    // Methods
    @Override
    public Corso createNewCorso(String nome, double price, int frequenza, String difficolta) {
        String sql = "INSERT INTO corso (nome_corso, costo, frequenza_settimanale, difficolta) " +
                "VALUES (?, ?, ?, ?::difficolta) RETURNING idcorso";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setBigDecimal(2, BigDecimal.valueOf(price));
            pstmt.setInt(3, frequenza);
            pstmt.setString(4, difficolta);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("idcorso");
                    return new Corso(id, nome, (float) price, frequenza, Difficolta.valueOf(difficolta));
                } else {
                    throw new SQLException("Creating corso failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addChefToCorso(int idCorso, Chef chef) {
        String checksql = "SELECT COUNT(*) FROM tiene WHERE idcorso = ? AND idchef = ?";
        String sql = "INSERT INTO tiene (idcorso, idchef) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(checksql)) {
            ps.setInt(1, idCorso);
            ps.setInt(2, chef.getIdchef());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                        pstmt.setInt(1, idCorso);
                        pstmt.setInt(2, chef.getIdchef());
                        int rowsInserted = pstmt.executeUpdate();
                        if (rowsInserted == 0) {
                            System.err.println("⚠️ Nessuna riga inserita in tiene");
                        }
                    }
                } else {
                    System.out.println("Chef già presente per questo corso → skip");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addToCaratterizzato(int idcorso, int idtipologia) {
        String sql = "INSERT INTO caratterizzato (idcorso, idtipologiacorso) VALUES (?,?)";

        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, idcorso);
            pstmt.setInt(2, idtipologia);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted == 0) {
                Exception exc  = new Exception("No row inserted");
                throw exc;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Corso corso) {
        String sql = "DELETE FROM Corso WHERE idcorso = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, corso.getIdCorso());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Corso corso) {
        String sql = "UPDATE corso " +
                "SET nome_corso = ?, costo = ?, difficolta = ?::difficolta, frequenza_settimanale = ? " +
                "WHERE idcorso = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, corso.getNome());
            ps.setBigDecimal(2, BigDecimal.valueOf(corso.getCosto()));
            ps.setString(3, corso.getDifficolta().name());
            ps.setInt(4, corso.getFrequenzaSettimanale());
            ps.setInt(5, corso.getIdCorso());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                System.err.println("Nessuna riga aggiornata! Controlla l'idcorso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void prepareChefs(int idCorso, int idChef) {
        String sql = "DELETE FROM tiene WHERE idcorso = ? AND idchef <> ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCorso);
            ps.setInt(2, idChef);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                System.err.println("Nessuna riga eliminata! Controlla l'idcorso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Get methods
    @Override
    public ArrayList<Corso> searchCorsiLikeString(String nomeCorso) throws corsiNotFoundException, SQLException {
        ArrayList<Corso> corsi = new ArrayList<>();
        String sql = "SELECT * FROM corso WHERE UPPER(nome_corso) LIKE ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeCorso.toUpperCase() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    corsi.add(createCorsoByResultSet(rs));
                }
            }
        }

        if (corsi.isEmpty())
            throw new corsiNotFoundException();

        return corsi;
    }

    @Override
    public ArrayList<Corso> searchCorsiByTipologia(String tipologia)throws corsiNotFoundException, SQLException{
        tipologia = tipologia.toUpperCase();
        ArrayList<Corso> corsi = new ArrayList<>();
        String sql = "select distinct c.idcorso, c.nome_corso, c.desc_corso, c.datainizio," +
                "c.datafine, c.costo,c.modcorso,c.difficolta,c.frequenza_settimanale,c.ore_totali, c.numerosessioni "+
                "from corso c natural join caratterizzato natural join tipologiacorso t where t.nome_tipo ilike ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tipologia + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                corsi.add(createCorsoByResultSet(rs));
            }
        return corsi;
    }

    @Override
    public ArrayList<Corso> searchCorsiByChef(String nomeChef)throws corsiNotFoundException,SQLException{
        nomeChef = nomeChef.toUpperCase();
        ArrayList<Corso> corsi = new ArrayList<>();
        String sql = "select distinct  c.idcorso, c.nome_corso, c.desc_corso, c.datainizio," +
                " c.datafine, c.costo,c.modcorso,c.difficolta,c.frequenza_settimanale,c.ore_totali, c.numerosessioni" +
                " from corso c natural join tiene natural join chef ch where ch.nome_chef ilike ? OR ch.cognome ilike ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + nomeChef + "%");
            stmt.setString(2, "%" + nomeChef + "%");
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                corsi.add( createCorsoByResultSet(rs));
            }
            if(corsi.isEmpty())
                throw new corsiNotFoundException();

        return corsi;
    }

    /*
    @Override
    public Corso getCorsoByResultSetWithOutSessioni(ResultSet rs)throws corsiNotFoundException, SQLException{

        Corso corso = new Corso(
                rs.getInt("idcorso"),
                rs.getString("nome_corso"),
                rs.getInt("numerosessioni"),
                rs.getFloat("ore_totali"),
                rs.getInt("frequenza_settimanale"),
                rs.getDate("datainizio"),
                rs.getDate("datafine"),
                rs.getFloat("costo"),
                ModalitaCorso.fromString( rs.getString("modcorso") ),
                Difficolta.valueOf(rs.getString("difficolta") ),
                rs.getString("desc_corso")
        );

        String nomeCorsoPulito = rs.getString("nome_corso").replaceAll("\\s+", "");
        corso.setImagePath("/Media/CoursesImages/" +nomeCorsoPulito+".png");
        setChefs(corso);

        return  corso;
    }

     */

    @Override
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

    @Override
    public ArrayList<Corso> getAllCourses(){
        String sql = "SELECT * FROM corso";
        ArrayList<Corso> corsi = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                corsi.add(createCorsoByResultSet(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return corsi;
    }

    @Override
    public Corso getCorsoByTitle(String Title){
        String sql = "SELECT * FROM corso WHERE nome_corso = ?";

        Corso corso = null;
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, Title);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    corso = createCorsoByResultSet(rs);
                } else {
                    System.out.println("Corso non trovato");
                }
            }
        } catch (SQLException sqle) {
            System.out.println("Errore nel cercare il corso");
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return corso;
    }

    @Override
    public void getRicetteTrattate(Corso corso) {
        corso.allocaArrayRicette();
        Ricetta ricetta = null;

        String sql = "SELECT DISTINCT idricetta, nome_ricetta, descrizione_ricetta, tempo_Di_Preparazione, autore " +
                "FROM corso " +
                "NATURAL JOIN sessione " +
                "NATURAL JOIN tratta " +
                "NATURAL JOIN ricetta " +
                "WHERE idcorso = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, corso.getIdCorso());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ricetta = new Ricetta(
                            rs.getInt("idricetta"),
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

    @Override
    public Corso getCorsoByIdCorso(int idcorso){
        String sql = "SELECT * FROM corso WHERE idcorso = ?";

      try {
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, idcorso);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            return createCorsoByResultSet(rs);

      } catch (SQLException sqle) {
          sqle.printStackTrace();
      }
        return null;
    }

    public Corso getCorsoByNome(String nome){
        String sql = "SELECT * FROM corso WHERE nome_corso = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                return createCorsoByResultSet(rs);

        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
        return null;
    }

    @Override
    public void setChefs(Corso corso) {
        corso.allocaArrayChefs();
        Chef chef = null;

        String sql = "SELECT DISTINCT idchef, nome_chef, cognome, email, passw " +
                "FROM chef " +
                "NATURAL JOIN tiene " +
                "NATURAL JOIN corso " +
                "WHERE idcorso = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, corso.getIdCorso());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chef = new Chef(
                            rs.getInt("idchef"),
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

//TODO o cess da cambiare
    private ArrayList<Sessione> getSessioniCorso(Corso corso)throws SQLException{
        SessioneDAO sessioneDAO = new SessioneDAO(controller);
        return sessioneDAO.getSessioniByCorso(corso);
    }

    @Override
    public ArrayList<Corso> getCorsiByModalita(String modalita){
        String sql = "SELECT * FROM corso WHERE modcorso = '"+modalita+"'";
        ArrayList<Corso> corsi = new ArrayList<>();
        try {
          PreparedStatement pstmt = con.prepareStatement(sql);
          ResultSet rs = pstmt.executeQuery();
          while (rs.next()) {
              corsi.add(createCorsoByResultSet(rs));
          }
        }catch (SQLException sqle){
            return null;
        }

        return corsi;
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
                ModalitaCorso.fromString( rs.getString("modcorso") ),
                Difficolta.valueOf(rs.getString("difficolta") ),
                rs.getString("desc_corso")
        );

        String nomeCorsoPulito = rs.getString("nome_corso").replaceAll("\\s+", "");
        corso.setImagePath("/Media/CoursesImages/" +nomeCorsoPulito+".png");
        System.out.println("sto mettendo sessioni");
        corso.setSessioni(this.getSessioniCorso(corso));
        setChefs(corso);

        return  corso;
    }
}
