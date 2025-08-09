package Entity;

import java.util.Date;

public class Corso {
    private String nome;
    private int numeroSessioni;
    private float oreTotali;
    private int frequenzaSettimanale;
    private Date dataInizio;
    private Date dataFine;
    private float costo;
    private String modalitaCorso; // calcolata su enum ModalitàSessione per tutte le sessioni
    private Difficolta difficolta;
}
