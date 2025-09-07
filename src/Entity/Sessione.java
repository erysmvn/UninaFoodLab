package Entity;

import DAO.ChefDAO;
import Entity.Enum.ModalitaSessione;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Sessione {
    protected LocalDate data;
    protected LocalDateTime orario;
    protected float durata;
    protected int idsessione;
    protected Corso corso;
    protected ArrayList<Ricetta> ricette;

    public Corso getCorso() {
        return corso;
    }

    public ArrayList<Ricetta> getRicette() {
        return ricette;
    }
    public void setRicette(ArrayList<Ricetta> ricette) {
        this.ricette = ricette;
    }

    public int getIdSessione() {
        return idsessione;
    }
    public void setIdsessione(int idsessione){
        this.idsessione = idsessione;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalDateTime getOra() {
        return orario;
    }

    public float getDurata() {
        return durata;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setOra(LocalDateTime orario) {
        this.orario = orario;
    }

    public void setDurata(float durata) {
        this.durata = durata;
    }
}
