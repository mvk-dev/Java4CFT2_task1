package task1;

public enum Currency {
    USD("Доллар США"),
    EUR("Евро"),
    RUB("Рубль");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Currency{"
                + this.name() +
                " (" + name +
                ")}";
    }
}
