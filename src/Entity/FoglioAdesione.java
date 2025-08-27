package Entity;

public class FoglioAdesione {
    private String documento;
    private String matricola;
    private int idsessione;
    public FoglioAdesione(int idsessione, String matricola, String documento) {
        this.idsessione = idsessione;
        this.matricola = matricola;
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    public String getMatricola() {
        return matricola;
    }
    public int getIdsessione() {
        return idsessione;
    }

}
