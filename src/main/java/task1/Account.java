package task1;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.*;

public class Account {
    @Getter
    private String ownerName;

    private final Map<Currency, Integer> currencies;
    private final Deque<Action> actionHistory;

    public Account(String ownerName) {
        checkAndSetOwnerName(ownerName);
        this.currencies = new HashMap<>();
        this.actionHistory = new ArrayDeque<>();
    }

    private Account(Account another) {
        this.ownerName = another.ownerName;
        this.currencies = new HashMap<>(another.currencies);
        this.actionHistory = new ArrayDeque<>(another.actionHistory);
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

        Action action = actionHistory.removeFirst();
        action.run();
    }

    public boolean isUndoAvailable() {
        return !actionHistory.isEmpty();
    }

    private void addAction(Action action) {
        actionHistory.addFirst(action);
    }

    public Restorable getSavepoint() {
        return new AccountSavepoint(this);
    }

    @Override
    public String toString() {
        return "Account{" +
                "ownerName='" + ownerName + '\'' +
                ", currencies=" + currencies +
                '}';
    }

    private static class AccountSavepoint implements Restorable {
        private final LocalDateTime savepointTime = LocalDateTime.now();
        private final Account original;
        private final Account savepoint;

        private AccountSavepoint(Account account) {
            original = account;
            savepoint = new Account(account);
        }

        @Override
        public void restore() {
            original.ownerName = this.savepoint.ownerName;
            original.currencies.clear();
            original.currencies.putAll(this.savepoint.currencies);

            original.actionHistory.clear();
            original.actionHistory.addAll(this.savepoint.actionHistory);
        }

        @Override
        public String toString() {
            return "Backup{" +
                    "savepointTime=" + savepointTime +
                    ", savepoint=" + savepoint +
                    '}';
        }
    }
}


