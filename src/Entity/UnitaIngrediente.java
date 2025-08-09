package Entity;

public enum UnitaIngrediente {
    Grammi("gr"),
    Chili("kg"),
    Millilitri("ml"),
    Litri("l"),
    Quantita("qta");

    private final String label;

    UnitaIngrediente(String label) {
        this.label = label;
    }
}
