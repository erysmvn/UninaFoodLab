package Entity;

import Entity.Enum.*;

import java.util.Date;

public class Corso {
    private int idcorso;
    private String nome;
    private int numeroSessioni;
    private float oreTotali;
    private int frequenzaSettimanale;
    private Date dataInizio;
    private Date dataFine;
    private float costo;
    private ModalitaCorso modalitaCorso;
    private String desc_corso;
    private Difficolta difficolta;
    private String imagePath;

    public Corso(int idcorso, String nome, int numeroSessioni, float oreTotali,
                 int frequenzaSettimanale, Date dataInizio, Date dataFine, float costo,
                 ModalitaCorso modalitaCorso,Difficolta difficolta, String desc_corso) {
        this.idcorso = idcorso;
        this.nome = nome;
        this.numeroSessioni = numeroSessioni;
        this.oreTotali = oreTotali;
        this.frequenzaSettimanale = frequenzaSettimanale;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.costo = costo;
        this.modalitaCorso = modalitaCorso;
        this.difficolta = difficolta;
        this.desc_corso = desc_corso;

    }
    public String getNome(){
        return nome;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }
    public String getImagePath() {
        return imagePath;
    }

    public ModalitaCorso getModalita_corso() {
        return  modalitaCorso;
    }
    public Difficolta getDifficolta() {
        return difficolta;
    }

    public Date getDataInizio() {
        return dataInizio;
    }
    public Date getDataFine() {
        return dataFine;
    }
    public float getCosto() {
        return costo;
    }

    public float getOreTotali() {
        return oreTotali;
    }

    public int getFrequenzaSettimanale(){
        return frequenzaSettimanale;
    }

    public String getDesc_corso() {
        return desc_corso;
    }

    public int getIdCorso() {
        return idcorso;
    }
}
