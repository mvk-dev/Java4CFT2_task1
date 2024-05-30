package task1;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Account {
    @Getter
    private String ownerName;

    private final Map<Currency, Integer> currencies = new HashMap<>();

    public Account(String ownerName) {
        setOwnerName(ownerName);
    }

    public void setOwnerName(String ownerName) {
        if (ownerName == null || ownerName.isEmpty())
            throw new IllegalArgumentException("Owner name can't be empty");

        this.ownerName = ownerName;
    }

    public Map<Currency, Integer> getCurrencies() {
        return new HashMap<>(currencies);
    }

    public void add(@NonNull Currency currency, @NonNull Integer count) {
        if (count < 0)
            throw new IllegalArgumentException("Currency count must be positive");

        currencies.put(currency, count);
    }

    @Override
    public String toString() {
        return "Account{" +
                "ownerName='" + ownerName + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}


