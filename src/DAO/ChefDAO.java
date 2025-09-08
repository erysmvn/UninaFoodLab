package DAO;

import DAO.Interfaces.ChefDAOInterface;
import DB.DBConnection;
import Entity.*;
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
        String sql = "INSERT INTO chef (nome_chef, cognome, email, passw) VALUES (?, ?, ?, md5(?))";

        PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, chef.getNome());
        pstmt.setString(2, chef.getCognome());
        pstmt.setString(3, chef.getEmail());
        pstmt.setString(4, chef.getPassw());

        int rowsInserted = pstmt.executeUpdate();

        if (rowsInserted == 0)
            throw new SQLException();


        ResultSet generatedKeys = pstmt.getGeneratedKeys();

        if (generatedKeys.next()){
            int id = generatedKeys.getInt("idchef");
            chef.setIdchef(id);
        } else {
            throw new SQLException();
        }

        return chef;
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

    public Map<String, Object> getReportMensile(Chef chef, YearMonth mese) throws SQLException {
        Map<String, Object> reportData = new HashMap<>();


        LocalDate inizioMese = mese.atDay(1);
        LocalDate fineMese = mese.atEndOfMonth();

        // Converti LocalDate in java.sql.Date
        Date sqlInizioMese = Date.valueOf(inizioMese);
        Date sqlFineMese = Date.valueOf(fineMese);

        // 1. Numero di corsi totali tenuti nel mese
        String sqlCorsiTotali = "SELECT COUNT(DISTINCT c.idcorso) as corsi_totali " +
                "FROM corso c " +
                "JOIN tiene t ON c.idcorso = t.idcorso " +
                "WHERE t.idchef = ? AND c.datainizio BETWEEN ? AND ?";

        // 2. Numero di sessioni online e pratiche
        String sqlSessioni = "SELECT " +
                "COUNT(CASE WHEN s.modalita = 'Online' THEN 1 END) as sessioni_online, " +
                "COUNT(CASE WHEN s.modalita = 'Presenza' THEN 1 END) as sessioni_pratiche " +
                "FROM sessione s " +
                "JOIN corso c ON s.idcorso = c.idcorso " +
                "JOIN tiene t ON c.idcorso = t.idcorso " +
                "WHERE t.idchef = ? AND s.data BETWEEN ? AND ?";

        // 3. Statistiche sulle ricette delle sessioni pratiche
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

        // 4. Dettaglio ricette per sessione pratica (per grafico a barre)
        String sqlRicettePerSessione = "SELECT s.idsessione, c.nome_corso, s.data, COUNT(r.idricetta) as num_ricette " +
                "FROM sessione s " +
                "JOIN tratta r ON s.idsessione = r.idsessione " +
                "JOIN corso c ON s.idcorso = c.idcorso " +
                "JOIN tiene t ON c.idcorso = t.idcorso " +
                "WHERE t.idchef = ? AND s.modalita = 'Presenza' " +
                "AND s.data BETWEEN ? AND ? " +
                "GROUP BY s.idsessione, c.nome_corso, s.data " +
                "ORDER BY s.data";

        try (PreparedStatement psCorsi = con.prepareStatement(sqlCorsiTotali);
             PreparedStatement psSessioni = con.prepareStatement(sqlSessioni);
             PreparedStatement psStats = con.prepareStatement(sqlStatisticheRicette);
             PreparedStatement psDettaglio = con.prepareStatement(sqlRicettePerSessione)) {

            // Imposta i parametri per tutte le query - CORRETTO
            psCorsi.setInt(1, chef.getIdchef());
            psCorsi.setDate(2, sqlInizioMese);
            psCorsi.setDate(3, sqlFineMese);

            psSessioni.setInt(1, chef.getIdchef());
            psSessioni.setDate(2, sqlInizioMese);
            psSessioni.setDate(3, sqlFineMese);

            psStats.setInt(1, chef.getIdchef());
            psStats.setDate(2, sqlInizioMese);
            psStats.setDate(3, sqlFineMese);

            psDettaglio.setInt(1, chef.getIdchef());
            psDettaglio.setDate(2, sqlInizioMese);
            psDettaglio.setDate(3, sqlFineMese);

            // Esegui query 1: corsi totali
            try (ResultSet rsCorsi = psCorsi.executeQuery()) {
                if (rsCorsi.next()) {
                    reportData.put("corsiTotali", rsCorsi.getInt("corsi_totali"));
                } else {
                    reportData.put("corsiTotali", 0);
                }
            }

            // Esegui query 2: sessioni online e pratiche
            try (ResultSet rsSessioni = psSessioni.executeQuery()) {
                if (rsSessioni.next()) {
                    reportData.put("sessioniOnline", rsSessioni.getInt("sessioni_online"));
                    reportData.put("sessioniPratiche", rsSessioni.getInt("sessioni_pratiche"));
                } else {
                    reportData.put("sessioniOnline", 0);
                    reportData.put("sessioniPratiche", 0);
                }
            }

            // Esegui query 3: statistiche ricette
            try (ResultSet rsStats = psStats.executeQuery()) {
                if (rsStats.next()) {
                    reportData.put("mediaRicette", rsStats.getDouble("media_ricette"));
                    reportData.put("maxRicette", rsStats.getInt("max_ricette"));
                    reportData.put("minRicette", rsStats.getInt("min_ricette"));
                } else {
                    reportData.put("mediaRicette", 0.0);
                    reportData.put("maxRicette", 0);
                    reportData.put("minRicette", 0);
                }
            }

            // Esegui query 4: dettaglio ricette per sessione (per grafico)
            Map<String, Integer> ricettePerSessione = new LinkedHashMap<>();
            try (ResultSet rsDettaglio = psDettaglio.executeQuery()) {
                while (rsDettaglio.next()) {
                    String nomeSessione = rsDettaglio.getString("nome_corso") + " - " +
                            rsDettaglio.getDate("data").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM"));
                    int numRicette = rsDettaglio.getInt("num_ricette");
                    ricettePerSessione.put(nomeSessione, numRicette);
                }
            }
            reportData.put("ricettePerSessione", ricettePerSessione);
        }

        // Aggiungi all'inizio del metodo
        System.out.println("Chef ID: " + chef.getIdchef());
        System.out.println("Mese: " + mese);
        System.out.println("Inizio mese: " + sqlInizioMese);
        System.out.println("Fine mese: " + sqlFineMese);

// E dopo ogni executeQuery, aggiungi:
        System.out.println("Query corsi eseguita");
// ... e così via per le altre query

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