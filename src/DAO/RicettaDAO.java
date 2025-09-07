package DAO;

import Controller.Controller;
import DAO.Interfaces.RicettaDAOInterface;
import DB.DBConnection;
import Entity.Enum.UnitaIngrediente;
import Entity.Ingrediente;
import Entity.IngredienteFormaRicetta;
import Entity.Ricetta;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RicettaDAO implements RicettaDAOInterface {
    DBConnection dbc;
    Connection con;

    // Constructors
    public RicettaDAO(DBConnection dbc) {
        this.dbc = dbc;
        con = dbc.getConnection();
    }

    @Override
    public void insertRicetta(Ricetta ricetta) throws SQLException {
        String sql = "INSERT INTO RICETTA(nome_ricetta, descrizione_ricetta,tempo_di_preparazione,autore) VALUES (?,?,?,?) RETURNING idricetta";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,ricetta.getNome());
        ps.setString(2,ricetta.getDescrizione());
        ps.setInt(3,ricetta.getTempoPreparazione());
        ps.setString(4,ricetta.getAutore());


        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("idricetta");
            ricetta.setIdRicetta(id);
        }
    }

    @Override
    public void inserisciIngredientiToRicetta(Ricetta ricetta) throws SQLException {
        String sql = "INSERT INTO FORMA (idRicetta, idIngrediente, unità, quantità) " +
                       "VALUES (?, ?, ?::unità_ingrediente, ?)";

        PreparedStatement pstmt = con.prepareStatement(sql);
        for (IngredienteFormaRicetta ingredienteFormaRicetta : ricetta.getIngredienteFormaRicetta()) {
            pstmt.setInt(1, ricetta.getIdRicetta());
            pstmt.setInt(2, ingredienteFormaRicetta.getIngrediente().getIdIngrediente());
            pstmt.setString(3, ingredienteFormaRicetta.getUnitaIngrediente().getDbValue());
            pstmt.setInt(4, ingredienteFormaRicetta.getQuantita());
            pstmt.executeUpdate();
        }
    }


    // Methods
    @Override
    public Ricetta createRicettaByResulSet(ResultSet rs) throws SQLException {
        Ricetta ricetta =  new Ricetta(
                rs.getInt("idricetta"),
                rs.getString("nome_ricetta"),
                rs.getString("descrizione_ricetta"),
                rs.getInt("tempo_di_preparazione"),
                rs.getString("autore")
        );

        getIngredienti(ricetta);
        getAllergeniRicetta(ricetta);
        return ricetta;
    }

    // Get Methods
    @Override
    public void getIngredienti(Ricetta ricetta) throws SQLException {
        ricetta.allocaArrayIngredienti();
        Ingrediente ingrediente;

        String sql = "SELECT DISTINCT idIngrediente, nome_ingrediente, allergeni, categoria,quantità ,unità as unita " +
                "FROM ingrediente " +
                "NATURAL JOIN forma " +
                "NATURAL JOIN ricetta " +
                "WHERE idricetta = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, ricetta.getIdRicetta());

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            ingrediente = new Ingrediente(
                    rs.getInt("idIngrediente"),
                    rs.getString("nome_ingrediente"),
                    rs.getString("allergeni"),
                    rs.getString("categoria")
            );
            ricetta.addIngrediente(ingrediente);
        }
    }


    private UnitaIngrediente getUnitaFromDb(String unitaDbValue) {
        for (UnitaIngrediente u : UnitaIngrediente.values()) {
            if (u.getDbValue().equals(unitaDbValue)) {
                return u;
            }
        }

        return UnitaIngrediente.Quantita;
    }


    @Override
    public ArrayList<Ricetta> getRicetteByIdSessione(int idsessione) throws SQLException{
        ArrayList<Ricetta> ricette = new ArrayList<>();
        String sql = "select * from ricetta natural join tratta natural join sessione s where idsessione = ?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idsessione);
        
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            ricette.add(createRicettaByResulSet(rs));
        }
        return ricette;
    }

    @Override
    public String getQuantitaIngrediente(Ricetta ricetta, Ingrediente ingrediente) throws SQLException {
        String sql = "SELECT quantità, unità " +
                "FROM forma " +
                "NATURAL JOIN ricetta " +
                "NATURAL JOIN ingrediente " +
                "WHERE idricetta = ? AND idingrediente = ?";

        String toReturn = "";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, ricetta.getIdRicetta());
        pstmt.setInt(2, ingrediente.getIdIngrediente());

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            toReturn = rs.getString("quantità") + " " + rs.getString("unità");
        }

        return toReturn;
    }

    @Override
    public void getAllergeniRicetta(Ricetta ricetta) throws SQLException {
        ricetta.allocaArrayAllergeniRicetta();
        String sql = "SELECT DISTINCT allergeni " +
                "FROM ingrediente " +
                "NATURAL JOIN forma " +
                "NATURAL JOIN ricetta " +
                "WHERE idricetta = ?";

        Set<String> allergeniSet = new HashSet<>();

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, ricetta.getIdRicetta());

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String allergeniStr = rs.getString("allergeni");
            if (allergeniStr != null && !allergeniStr.isEmpty()) {
                String[] allergeniArray = allergeniStr.split("\\s*,\\s*");
                for (String allergene : allergeniArray) {
                    if (!"Nessuno".equalsIgnoreCase(allergene)) {
                        allergeniSet.add(allergene);
                    }
                }
            }
        }

        for (String allergene : allergeniSet) {
            ricetta.addAllergeniRicetta(allergene);
        }
    }
    
}