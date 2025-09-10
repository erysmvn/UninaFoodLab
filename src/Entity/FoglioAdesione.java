package Entity;

public class FoglioAdesione {
    private String documento;
    private Studente studente;
    private SessionePresenza sessionePresenza;

    public FoglioAdesione(SessionePresenza sessionePresenza, Studente studente, String documento) {
        this.sessionePresenza = sessionePresenza;
        this.studente = studente;
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getMatricola() {
        return studente.getMatricola();
    }

    public int getIdsessione() {
        return sessionePresenza.idsessione;
    }

}
