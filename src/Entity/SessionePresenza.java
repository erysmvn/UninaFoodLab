package Entity;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class SessionePresenza extends Sessione {
    private String luogo;
    private ArrayList<FoglioAdesione> fogliAdesione;
    public SessionePresenza(int idsessione, LocalDate data, String luogo, float durata, LocalDateTime ora, Corso corso) {
        this.idsessione = idsessione;
        this.data = data;
        this.luogo = luogo;
        this.durata = durata;
        this.orario = ora;
        this.corso = corso;
    }

    public SessionePresenza(LocalDate data, String luogo, float durata, LocalDateTime ora, Corso corso) {
        this.data = data;
        this.luogo = luogo;
        this.durata = durata;
        this.orario = ora;
        this.corso = corso;
    }

    public ArrayList<FoglioAdesione> getFogliAdesione() {
        return fogliAdesione;
    }

    public void setFogliAdesione(ArrayList<FoglioAdesione> fogliAdesione) {
        this.fogliAdesione = fogliAdesione;
    }
    public void addFoglioAdesione(FoglioAdesione foglio) {
        this.fogliAdesione.add(foglio);
    }

    public boolean checkIfAlreadyAdded(String matricola){
        for(FoglioAdesione foglio : fogliAdesione){

            if(foglio.getMatricola().equals(matricola))
                 return true;

        }
        return false;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

}

