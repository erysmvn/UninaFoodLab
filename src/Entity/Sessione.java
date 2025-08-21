package Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Sessione {
    protected LocalDate data;
    protected LocalDateTime orario;
    protected float durata;

    public LocalDate getData() {
        return data;
    }

    public LocalDateTime getOra() {
        return orario;
    }

    public float getDurata() {
        return durata;
    }
}
