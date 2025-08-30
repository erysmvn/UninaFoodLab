package Entity.Enum;

public enum UnitaIngrediente {
    Grammi("Grammi (gr)"),
    Chili("Chili (kg)"),
    Millilitri("Millilitri (ml)"),
    Litri("Litri (l)"),
    Quantita("Quantità (qta)");

    private final String dbValue;

    UnitaIngrediente(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}
