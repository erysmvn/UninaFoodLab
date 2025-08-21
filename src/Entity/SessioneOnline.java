package Entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class SessioneOnline extends Sessione{
    private String linkIncontro;


    public SessioneOnline(String linkIncontro, float durata, LocalDateTime ora ) {
        this.linkIncontro = linkIncontro;
        this.durata = durata;
        this.orario = ora;
    }

    public String getLinkIncontro() {
        return linkIncontro;
    }
}
