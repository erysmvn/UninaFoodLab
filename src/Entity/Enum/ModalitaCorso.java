package Entity.Enum;

public enum ModalitaCorso {
    Online("Online"),
    OnlineEInPresenza("Online e in presenza"),
    Presenza("Presenza");

    private final String label;

    ModalitaCorso(String label) {
        this.label = label;
    }

    public static ModalitaCorso getFromString(String label) {
        if (label == "Online e in presenza") {
            return OnlineEInPresenza;
        } else if(label == "Presenza") {
            return Presenza;
        }else{
            return Online;
        }
    }

    public String getLabel(){
        return label;
    }
}
