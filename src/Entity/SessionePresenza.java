package Entity;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class SessionePresenza extends Sessione {
    private String luogo;

    public SessionePresenza(int idsessione, LocalDate data, String luogo, float durata, LocalDateTime ora, Corso corso) {
        this.idsessione = idsessione;
        this.data = data;
        this.luogo = luogo;
        this.durata = durata;
        this.orario = ora;
        this.corso = corso;
    }

    public String getLuogo() {
        return luogo;
    }

}

