package Entity;


import java.time.LocalDateTime;


public class SessionePresenza extends Sessione {
    private String luogo;

    public SessionePresenza(String luogo, float durata, LocalDateTime ora ) {
        this.luogo = luogo;
        this.durata = durata;
        this.orario = ora;
    }

    public String getLuogo() {
        return luogo;
    }

}

