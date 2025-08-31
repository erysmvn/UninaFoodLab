package Entity;

import Entity.Enum.*;

import java.io.File;
import java.util.ArrayList;
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

    private ArrayList<Chef> chefs = null;
    private ArrayList<Ricetta> ricetteTrattate = null;
    private ArrayList<Sessione> sessioni = null;


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

    public Corso(int id, String nome, float costo, int frequenzaSettimanale, Difficolta difficolta) {
        this.idcorso = id;
        this.nome = nome;
        this.frequenzaSettimanale = frequenzaSettimanale;
        this.costo = costo;
        this.difficolta = difficolta;
        String nameForPath = nome.replaceAll("\s+", "");
        this.imagePath = "src/Media/CoursesImages/" + nameForPath + ".png";
    }

    public void allocaArrayChefs() {
        this.chefs = new ArrayList<Chef>();
    }

    public void addChef(Chef chef) {
        chefs.add(chef);
    }

    public void allocaArrayRicette() {
        this.ricetteTrattate = new ArrayList<Ricetta>();
    }

    public void addRicetta(Ricetta ricetta) {
        ricetteTrattate.add(ricetta);
    }

    // Getter
    public String getNome(){
        return nome;
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

    public String getStringOfChefs() {
        String toReturn = "";
        for (Chef chef: chefs) {
            toReturn += chef.getNome() + " " + chef.getCognome() + ", ";
        }
        if (toReturn == "") {
            return "";
        }
        return toReturn.substring(0, toReturn.length() - 2);
    }

    public ArrayList<Chef> getChefs() {
        return chefs;
    }

    public ArrayList<Ricetta> getRicetteTrattate() {
        return ricetteTrattate;
    }

    public ArrayList<Sessione> getSessioni() {
        return sessioni;
    }

    // Setter
    public void setSessioni(ArrayList<Sessione> sessioni) {
        this.sessioni = sessioni;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public void setNome(String nome) {
        String oldNameForPath = this.nome.replaceAll("\\s+", "");
        String oldPath = "src/Media/CoursesImages/" + oldNameForPath + ".png";

        this.nome = nome;

        String newNameForPath = nome.replaceAll("\\s+", "");
        String newPath = "src/Media/CoursesImages/" + newNameForPath + ".png";
        this.imagePath = newPath;

        File oldFile = new File(oldPath);
        File newFile = new File(newPath);

        if (oldFile.exists()) {
            boolean success = oldFile.renameTo(newFile);
            if (!success) {
                System.err.println("Errore nel rinominare l'immagine da " + oldPath + " a " + newPath);
            }
        } else {
            System.err.println("File immagine non trovato: " + oldPath);
        }
    }

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    public void setFrequenzaSettimanale(int frequenzaSettimanale) {
        this.frequenzaSettimanale = frequenzaSettimanale;
    }

    public void setCosto (float costo) {
        this.costo = costo;
    }

    public void setChefs(ArrayList<Chef> chefs) {
        this.chefs = chefs;
    }

}
