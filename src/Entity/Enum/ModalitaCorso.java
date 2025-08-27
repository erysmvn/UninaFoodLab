package Entity.Enum;

public enum ModalitaCorso {
    Online("Online"),
    OnlineEInPresenza("Online e in presenza"),
    Presenza("Presenza");

    private final String label;

    ModalitaCorso(String label) {
        this.label = label;
    }

    public static ModalitaCorso fromString(String label) {
        if (label == null) return null;
        String cleanLabel = label.trim().toLowerCase();
        switch (cleanLabel) {
            case "online":
                return Online;
            case "online e in presenza":
                return OnlineEInPresenza;
            case "presenza":
                return Presenza;
            default:
                return null;
        }
    }

    public String getLabel() {
        return label;
    }
}