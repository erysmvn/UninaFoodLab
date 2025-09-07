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

    // Constructors
    public CorsoDAO(DBConnection dbc) {
        this.dbc = dbc;
        con = dbc.getConnection();
    }

    // Methods
    @Override
    public Corso createNewCorso(String nome, double price, int frequenza, String difficolta) throws SQLException {
        String sql = "INSERT INTO corso (nome_corso, costo, frequenza_settimanale, difficolta) " +
                "VALUES (?, ?, ?, ?::difficolta) RETURNING idcorso";

        PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setBigDecimal(2, BigDecimal.valueOf(price));
            pstmt.setInt(3, frequenza);
            pstmt.setString(4, difficolta);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("idcorso");
            return new Corso(id, nome, (float) price, frequenza, Difficolta.valueOf(difficolta));
        } else {
            throw new SQLException("Creating corso failed, no ID obtained.");
        }
    }

    @Override
    public void addChefToCorso(int idCorso, Chef chef) throws SQLException {
        String checksql = "SELECT COUNT(*) FROM tiene WHERE idcorso = ? AND idchef = ?";
        String sql = "INSERT INTO tiene (idcorso, idchef) VALUES (?, ?)";

        PreparedStatement ps = con.prepareStatement(checksql);
        ps.setInt(1, idCorso);
        ps.setInt(2, chef.getIdchef());

        ResultSet rs = ps.executeQuery();
        if (rs.next() && rs.getInt(1) == 0) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, idCorso);
            pstmt.setInt(2, chef.getIdchef());
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException();
            }
        }
    }

    @Override
    public void addToCaratterizzato(int idcorso, int idtipologia) throws SQLException {
        String sql = "INSERT INTO caratterizzato (idcorso, idtipologiacorso) VALUES (?,?)";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, idcorso);
        pstmt.setInt(2, idtipologia);

        int rowsInserted = pstmt.executeUpdate();
        if (rowsInserted == 0) {
            throw new SQLException("No row inserted");
        }
    }

    @Override
    public void delete(Corso corso) throws SQLException {
        String sql = "DELETE FROM Corso WHERE idcorso = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, corso.getIdCorso());
        pstmt.executeUpdate();
    }

    public void update(Corso corso) throws SQLException {
        String sql = "UPDATE corso " +
                "SET nome_corso = ?, costo = ?, difficolta = ?::difficolta, frequenza_settimanale = ? " +
                "WHERE idcorso = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, corso.getNome());
        ps.setBigDecimal(2, BigDecimal.valueOf(corso.getCosto()));
        ps.setString(3, corso.getDifficolta().name());
        ps.setInt(4, corso.getFrequenzaSettimanale());
        ps.setInt(5, corso.getIdCorso());

        int rows = ps.executeUpdate();
        if (rows == 0) {
            System.err.println("Nessuna riga aggiornata! Controlla l'idcorso.");
        }
    }

    @Override
    public void deleteOtherChefs(int idCorso, int idChefNotToDelete) throws SQLException {
        String sql = "DELETE FROM tiene WHERE idcorso = ? AND idchef <> ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, idCorso);
        ps.setInt(2, idChefNotToDelete);

        int rows = ps.executeUpdate();
        if (rows == 0) {
            throw new SQLException("Nessuna riga eliminata!");
        }
    }


    // Get methods
    @Override
    public ArrayList<Corso> searchCorsiLikeNomeCorso(String nomeCorso) throws SQLException, corsiNotFoundException {
        ArrayList<Corso> corsi = new ArrayList<>();
        String sql = "SELECT * FROM corso WHERE UPPER(nome_corso) LIKE ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, "%" + nomeCorso.toUpperCase() + "%");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            corsi.add(createCorsoByResultSet(rs));
        }

        if (corsi.isEmpty())
            throw new corsiNotFoundException();

        return corsi;
    }

    @Override
    public ArrayList<Corso> searchCorsiLikeNomeTipologia(String tipologia) throws SQLException, corsiNotFoundException {
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
    public ArrayList<Corso> searchCorsiLikeNomeChef(String nomeChef)throws SQLException, corsiNotFoundException {
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


    @Override
    public ArrayList<Corso> getCorsiConPiuStudenti(int numeroCorsiLimite) throws SQLException, corsiNotFoundException {
        ArrayList<Corso> corsi = new ArrayList<>();
        String sql = "SELECT corso.nome_corso, count(segue.matricola) as NumStudenti " +
                "FROM corso NATURAL JOIN segue NATURAL JOIN studente " +
                "GROUP BY corso.idCorso, corso.nome_corso " +
                "ORDER BY NumStudenti DESC LIMIT ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, numeroCorsiLimite);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Corso corso = getCorsoByTitle(rs.getString("nome_corso"));
            if (corso != null) {
                corsi.add(corso);
            }
        }
        return corsi;
    }

    @Override
    public ArrayList<Corso> getAllCourses() throws SQLException {
        String sql = "SELECT * FROM corso";
        ArrayList<Corso> corsi = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            corsi.add(createCorsoByResultSet(rs));
        }
        return corsi;
    }

    @Override
    public Corso getCorsoByTitle(String Title) throws SQLException, corsiNotFoundException {
        String sql = "SELECT * FROM corso WHERE nome_corso = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, Title);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return createCorsoByResultSet(rs);
        } else {
            throw new corsiNotFoundException();
        }
    }

    @Override
    public void getRicetteTrattate(Corso corso) throws SQLException {
        corso.allocaArrayRicette();

        String sql = "SELECT DISTINCT idricetta, nome_ricetta, descrizione_ricetta, tempo_Di_Preparazione, autore " +
                "FROM corso " +
                "NATURAL JOIN sessione " +
                "NATURAL JOIN tratta " +
                "NATURAL JOIN ricetta " +
                "WHERE idcorso = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, corso.getIdCorso());

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Ricetta ricetta = new Ricetta(
                    rs.getInt("idricetta"),
                    rs.getString("nome_ricetta"),
                    rs.getString("descrizione_ricetta"),
                    rs.getInt("tempo_Di_Preparazione"),
                    rs.getString("autore")
            );
            corso.addRicetta(ricetta);
        }
    }

    @Override
    public Corso    getCorsoByIdCorso(int idcorso) throws SQLException {
        String sql = "SELECT * FROM corso WHERE idcorso = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, idcorso);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            return createCorsoByResultSet(rs);

        return null;
    }

    public Corso getCorsoByNome(String nome) throws SQLException {
        String sql = "SELECT * FROM corso WHERE nome_corso = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, nome);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            return createCorsoByResultSet(rs);

        return null;
    }

    @Override
    public void setChefs(Corso corso) throws SQLException {
        corso.allocaArrayChefs();

        String sql = "SELECT DISTINCT idchef, nome_chef, cognome, email, passw " +
                "FROM chef " +
                "NATURAL JOIN tiene " +
                "NATURAL JOIN corso " +
                "WHERE idcorso = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, corso.getIdCorso());

        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Chef chef = new Chef(
                    rs.getInt("idchef"),
                    rs.getString("nome_chef"),
                    rs.getString("cognome"),
                    rs.getString("email"),
                    rs.getString("passw")
            );
            corso.addChef(chef);
        }
    }

    private Corso createCorsoByResultSet(ResultSet rs) throws SQLException {
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

        SessioneDAO sessioneDAO = new SessioneDAO(dbc);
        corso.setSessioni(sessioneDAO.getSessioniByCorso(corso));

        setChefs(corso);

        return  corso;
    }

}
