package task1;

import lombok.Getter;
import lombok.NonNull;

import java.util.*;

public class Account {
    @Getter
    private String ownerName;

    private final Map<Currency, Integer> currencies = new HashMap<>();
    private final Stack<Action> actionHistory = new Stack<>();

    public Account(String ownerName) {
        checkAndSetOwnerName(ownerName);
    }

    public Map<Currency, Integer> getCurrencies() {
        return new HashMap<>(currencies);
    }

    public void setOwnerName(String ownerName) {
        String oldName = this.ownerName;
        checkAndSetOwnerName(ownerName);
        addAction(() -> this.ownerName = oldName);
    }

    private void checkAndSetOwnerName(String ownerName) {
        if (ownerName == null || ownerName.isEmpty())
            throw new IllegalArgumentException("Owner name can't be empty");

        this.ownerName = ownerName;
    }

    public void add(@NonNull Currency currencyKey, @NonNull Integer count) {
        if (count < 0)
            throw new IllegalArgumentException("Currency count must be positive");

        if (currencies.containsKey(currencyKey)) {
            Map.Entry<Currency, Integer> entry = Map.entry(currencyKey, currencies.get(currencyKey));
            addAction(() -> currencies.put(entry.getKey(), entry.getValue()));
        } else
            addAction(() -> currencies.remove(currencyKey));

        currencies.put(currencyKey, count);
    }

    public void undo() {
        if (!isUndoAvailable())
            throw new RuntimeException("Undo is not available");

        // Берем последнее действие и отменяем
        Action action = actionHistory.pop();
        action.run();
    }

    public boolean isUndoAvailable() {
        return !actionHistory.isEmpty();
    }

    private void addAction(Action action) {
        actionHistory.add(action);
    }

    @Override
    public String toString() {
        return "Account{" +
                "ownerName='" + ownerName + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}


