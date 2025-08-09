package Entity;

public enum Difficolta {
    Base("Base"),
    Intermedio("Intermedio"),
    Avanzato("Avanzato");

    private final String label;

    Difficolta(String label) {
        this.label = label;
    }
}
