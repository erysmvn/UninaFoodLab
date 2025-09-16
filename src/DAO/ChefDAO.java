package DAO;

import DAO.Interfaces.ChefDAOInterface;
import DB.DBConnection;
import Entity.*;
import Exception.CorsoExceptions.noCorsiTenutiException;
import Exception.UserExceptions.ChangePasswordException.changePasswordException;
import Exception.UserExceptions.ChangePasswordException.oldPasswordErrorException;
import Exception.UserExceptions.LoginException.emailNotFoundException;
import Exception.UserExceptions.LoginException.passwordErrataException;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChefDAO implements ChefDAOInterface {
    DBConnection dbc;
    Connection con;

    // Constructors
    public ChefDAO(DBConnection dbc) {
        this.dbc = dbc;
        this.con = dbc.getConnection();
    }


    // Methods
    @Override
    public Chef login(String email, String password) throws emailNotFoundException, passwordErrataException,SQLException{
        Chef chef;
        email = email.trim();

        String sql = "Select * from chef where email = ? AND  passw = md5(?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if(rs.next()){
            chef = createChefByRs(rs);
        } else {
            if (existingEmail(email))
                throw new passwordErrataException();
            else
                throw new emailNotFoundException();
        }
        return chef;
    }

    @Override
    public Chef register(Chef chef) throws SQLException {
        String sql = "INSERT INTO chef (nome_chef, cognome, email, passw) VALUES (?, ?, ?, md5(?)) RETURNING idchef";

        PreparedStatement pstmt = con.prepareStatement(sql);

        pstmt.setString(1, chef.getNome());
        pstmt.setString(2, chef.getCognome());
        pstmt.setString(3, chef.getEmail());
        pstmt.setString(4, chef.getPassw());

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("idchef");
            chef.setIdchef(id);
            return chef;
        } else {
            throw new SQLException();
        }
    }

    @Override
    public void checkOldPassword(String oldPassword, Chef chef) throws SQLException, changePasswordException {
        String sql = "SELECT 1 FROM chef WHERE passw = md5(?) AND idchef = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, oldPassword);
        ps.setInt(2, chef.getIdchef());

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            throw new oldPasswordErrorException();
        }
    }

    @Override
    public void changeUserPassword(String newPassword, Chef chef) throws changePasswordException, SQLException {
        String sql = "UPDATE chef SET passw = md5(?) WHERE idchef = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, newPassword);
        ps.setInt(2, chef.getIdchef());
        int rows = ps.executeUpdate();

        if (rows > 0) {
            chef.setPassw(newPassword);
        } else {
            throw new changePasswordException();
        }
    }

    // Get methods
    @Override
    public void setCorsiToChef(Chef chef) throws SQLException {
        String sql = "SELECT DISTINCT c.idcorso " +
                "FROM tiene NATURAL JOIN chef ch NATURAL JOIN corso c " +
                "WHERE ch.idchef = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, chef.getIdchef());
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            CorsoDAO corsoDAO = new CorsoDAO(dbc);
            Corso corso = corsoDAO.getCorsoByIdCorso(rs.getInt("idcorso"));
            chef.getCorsi().add(corso);
        }
    }

    @Override
    public Chef getChefDaAggiungereToNuovoCorso(String nome, String cognome, String email) throws SQLException {
        String sql = "SELECT * FROM chef WHERE UPPER(nome_chef) = UPPER(?) AND UPPER(cognome) = UPPER(?) AND UPPER(email) = UPPER(?);";
        
        PreparedStatement pstmt = con.prepareStatement(sql);

        pstmt.setString(1, nome);
        pstmt.setString(2, cognome);
        pstmt.setString(3, email);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return createChefByRs(rs);
        }

        return null;
    }

    private boolean existingEmail(String email) throws SQLException {
        String sql = "Select 1 from chef where email = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, email);

        ResultSet rs = pstmt.executeQuery();

        return rs.next();
    }

    private int getNumCorsiTenutiInData(Chef chef,Date inizioMese, Date fineMese ) throws SQLException, noCorsiTenutiException {


        String sqlCorsiTotali = "SELECT COUNT(DISTINCT c.idcorso) as corsi_totali " +
                "FROM corso c " +
                "JOIN tiene t ON c.idcorso = t.idcorso " +
                "WHERE t.idchef = ? AND c.datainizio BETWEEN ? AND ?";

        PreparedStatement psCorsi = con.prepareStatement(sqlCorsiTotali);

        psCorsi.setInt(1, chef.getIdchef());
        psCorsi.setDate(2, inizioMese);
        psCorsi.setDate(3, fineMese);

        ResultSet rsCorsi = psCorsi.executeQuery();

        int numCorsiTenuti = 0;

        if (rsCorsi.next())
            numCorsiTenuti = rsCorsi.getInt("corsi_totali");

        if(numCorsiTenuti == 0)
            throw new noCorsiTenutiException();

        return numCorsiTenuti;
    }
    private ResultSet getSessioniInData(Chef chef, Date inizioMese, Date fineMese) throws SQLException {

        String sqlSessioni = "SELECT " +
                "COUNT(CASE WHEN s.modalita = 'Online' THEN 1 END) as sessioni_online, " +
                "COUNT(CASE WHEN s.modalita = 'Presenza' THEN 1 END) as sessioni_pratiche " +
                "FROM sessione s " +
                "JOIN corso c ON s.idcorso = c.idcorso " +
                "JOIN tiene t ON c.idcorso = t.idcorso " +
                "WHERE t.idchef = ? AND s.data BETWEEN ? AND ?";

        PreparedStatement psSessioni = con.prepareStatement(sqlSessioni);
        psSessioni.setInt(1, chef.getIdchef());
        psSessioni.setDate(2, inizioMese);
        psSessioni.setDate(3, fineMese);

        return psSessioni.executeQuery();

    }
    private ResultSet getStatisticheRicette(Chef chef, Date inizioMese, Date fineMese) throws SQLException {

        String sqlStatisticheRicette = "SELECT " +
                "COALESCE(AVG(rc.conteggio), 0) as media_ricette, " +
                "COALESCE(MAX(rc.conteggio), 0) as max_ricette, " +
                "COALESCE(MIN(rc.conteggio), 0) as min_ricette " +
                "FROM ( " +
                "    SELECT s.idsessione, COUNT(r.idricetta) as conteggio " +
                "    FROM sessione s " +
                "    JOIN tratta r ON s.idsessione = r.idsessione " +
                "    JOIN corso c ON s.idcorso = c.idcorso " +
                "    JOIN tiene t ON c.idcorso = t.idcorso " +
                "    WHERE t.idchef = ? AND s.modalita = 'Presenza' " +
                "    AND s.data BETWEEN ? AND ? " +
                "    GROUP BY s.idsessione " +
                ") rc";

        PreparedStatement psStats = con.prepareStatement(sqlStatisticheRicette);
        psStats.setInt(1, chef.getIdchef());
        psStats.setDate(2, inizioMese);
        psStats.setDate(3, fineMese);

        return psStats.executeQuery();

    }
    private ResultSet getRicettePerSessioniPratiche(Chef chef, Date inizioMese, Date fineMese) throws SQLException {

        String sqlRicettePerSessione = "SELECT s.idsessione, c.nome_corso, s.data, COUNT(r.idricetta) as num_ricette " +
                "FROM sessione s " +
                "JOIN tratta r ON s.idsessione = r.idsessione " +
                "JOIN corso c ON s.idcorso = c.idcorso " +
                "JOIN tiene t ON c.idcorso = t.idcorso " +
                "WHERE t.idchef = ? AND s.modalita = 'Presenza' " +
                "AND s.data BETWEEN ? AND ? " +
                "GROUP BY s.idsessione, c.nome_corso, s.data " +
                "ORDER BY s.data";

        PreparedStatement psDettaglio = con.prepareStatement(sqlRicettePerSessione);
        psDettaglio.setInt(1, chef.getIdchef());
        psDettaglio.setDate(2, inizioMese);
        psDettaglio.setDate(3, fineMese);

        return psDettaglio.executeQuery();
    }

    @Override
    public Map<String, Object> getReportMensile(Chef chef, YearMonth mese) throws SQLException, noCorsiTenutiException {
        Map<String, Object> reportData = new HashMap<>();

       LocalDate inizioMese = mese.atDay(1);
       LocalDate fineMese = mese.atEndOfMonth();

       Date sqlInizioMese = Date.valueOf(inizioMese);
       Date sqlFineMese = Date.valueOf(fineMese);


       reportData.put("corsiTotali", getNumCorsiTenutiInData(chef, sqlInizioMese, sqlFineMese));

       ResultSet rsSessioni = getSessioniInData(chef,sqlInizioMese, sqlFineMese);
       if (rsSessioni.next()){
            reportData.put("sessioniOnline", rsSessioni.getInt("sessioni_online"));
            reportData.put("sessioniPratiche", rsSessioni.getInt("sessioni_pratiche"));
        } else {
            reportData.put("sessioniOnline", 0);
            reportData.put("sessioniPratiche", 0);
        }

       ResultSet rsStats = getStatisticheRicette(chef, sqlInizioMese, sqlFineMese);
       if (rsStats.next()) {
            reportData.put("mediaRicette", rsStats.getDouble("media_ricette"));
            reportData.put("maxRicette", rsStats.getInt("max_ricette"));
            reportData.put("minRicette", rsStats.getInt("min_ricette"));
       } else {
            reportData.put("mediaRicette", 0.0);
            reportData.put("maxRicette", 0);
            reportData.put("minRicette", 0);
       }

       Map<String, Integer> ricettePerSessione = new LinkedHashMap<>();
       ResultSet rsDettaglio = getRicettePerSessioniPratiche(chef, sqlInizioMese, sqlFineMese);
       while (rsDettaglio.next()) {
           String nomeSessione = rsDettaglio.getString("nome_corso") + " - " +
           rsDettaglio.getDate("data").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM"));
           int numRicette = rsDettaglio.getInt("num_ricette");
           ricettePerSessione.put(nomeSessione, numRicette);
       }

       reportData.put("ricettePerSessione", ricettePerSessione);

       return reportData;
    }

    private Chef createChefByRs(ResultSet rs) throws SQLException {
        Chef chef = new Chef(
                rs.getInt("idchef"),
                rs.getString("nome_chef"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("passw")
        );
        setCorsiToChef(chef);
        return chef;
    }

}