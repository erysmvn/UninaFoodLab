package Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class SessioneOnline extends Sessione{
    private String linkIncontro;


    public SessioneOnline(int idsessione, LocalDate data, String linkIncontro, float durata, LocalDateTime ora, Corso corso ) {
        this.idsessione = idsessione;
        this.data = data;
        this.linkIncontro = linkIncontro;
        this.durata = durata;
        this.orario = ora;
        this.corso = corso;
    }

    public SessioneOnline( LocalDate data, String linkIncontro, float durata, LocalDateTime ora, Corso corso ) {
        this.data = data;
        this.linkIncontro = linkIncontro;
        this.durata = durata;
        this.orario = ora;
        this.corso = corso;
    }



    public String getLinkIncontro() {
        return linkIncontro;
    }

    public void setLinkIncontro(String linkIncontro) {
        this.linkIncontro = linkIncontro;
    }
}
