package task1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class AccountTests {
    @Test
    void should_ThrowException_When_OwnerNameIsEmptyOrNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account((String) null));
    }

    @Test
    void should_ThrowException_When_ChangingOwnerNameToEmptyOrNull() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Account("Owner").setOwnerName(null));
    }

    @Test
    void should_ThrowException_When_AddCurrencyWithNullCurrency() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Account account = new Account("Owner");
            account.add(null, 1);
        });
    }

    @Test
    void should_ThrowException_When_AddCurrencyWithNullCount() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Account account = new Account("Owner");
            account.add(Currency.EUR, null);
        });
    }

    @Test
    void should_ThrowException_When_AddCurrencyWithNegativeCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Account account = new Account("Owner");
            account.add(Currency.EUR, -1);
        });
    }

    @Test
    void should_Success_When_AddCurrencyWithPositiveCount() {
        final int count = 5;

        Account account = new Account("Owner");
        account.add(Currency.EUR, count);

        Assertions.assertTrue(account.getCurrencies().containsKey(Currency.EUR));
        Assertions.assertEquals(count, account.getCurrencies().get(Currency.EUR));
    }

    @Test
    void should_ThrowException_When_CallingNotAvailableUndo() {
        Assertions.assertThrows(RuntimeException.class, () -> new Account("Owner").undo());
    }

    @Test
    void should_ReturnFalse_When_UndoIsNotAvailable() {
        Assertions.assertFalse(new Account("Owner").isUndoAvailable());
    }

    @Test
    void should_Success_When_UndoNameChange() {
        String initialName = "Jane Smith";
        String changedName1 = "Jane Johns";
        String changedName2 = "Jane Dow";

        Account account = new Account(initialName);

        account.setOwnerName(changedName1);
        Assertions.assertEquals(changedName1, account.getOwnerName());

        account.setOwnerName(changedName2);
        Assertions.assertEquals(changedName2, account.getOwnerName());

        account.undo();
        account.undo();
        Assertions.assertEquals(initialName, account.getOwnerName());
    }

    @Test
    void should_Success_When_UndoCurrencyChange() {
        String initialName = "Jane Smith";
        int count1 = 100;
        int count2 = 200;

        Account account = new Account(initialName);
        account.add(Currency.USD, count1);
        account.add(Currency.USD, count2);

        Assertions.assertEquals(count2, account.getCurrencies().get(Currency.USD));
        account.undo();
        Assertions.assertEquals(count1, account.getCurrencies().get(Currency.USD));
        account.undo();
        Assertions.assertNull(account.getCurrencies().get(Currency.USD));
    }

    @Test
    void should_Success_when_RestoreAccountFromSavepoint() {
        String initialName = "Jane Smith";
        Map.Entry<Currency, Integer> entry = Map.entry(Currency.USD, 10);
        Map<Currency, Integer> initialCurrencies = Map.of(entry.getKey(), entry.getValue());

        Account account = new Account(initialName);
        account.add(entry.getKey(), entry.getValue());
        Restorable savepoint = account.getSavepoint();

        account.add(Currency.USD, 101);
        account.add(Currency.EUR, 201);
        account.setOwnerName("Jane Wilson");
        savepoint.restore();

        Assertions.assertEquals(initialName, account.getOwnerName());
        Assertions.assertTrue(initialCurrencies.equals(account.getCurrencies()));
    }

    @Test
    void should_Success_when_RestoreAccountFromMultipleSavepoints() {
        String name1 = "Jane Smith";
        Map.Entry<Currency, Integer> entry1 = Map.entry(Currency.USD, 10);
        Map<Currency, Integer> currencies1 = Map.of(entry1.getKey(), entry1.getValue());

        String name2 = "Jane Wilson";
        Map.Entry<Currency, Integer> entry2 = Map.entry(Currency.EUR, 24);
        Map<Currency, Integer> currencies2 = Map.of(
                entry1.getKey(), entry1.getValue(),
                entry2.getKey(), entry2.getValue());

        Account account = new Account(name1);
        account.add(entry1.getKey(), entry1.getValue());
        // 1st savepoint
        Restorable savepoint1 = account.getSavepoint();

        // Добавляем валюту, меняем имя
        account.add(entry2.getKey(), entry2.getValue());
        account.setOwnerName(name2);
        // 2st savepoint
        Restorable savepoint2 = account.getSavepoint();

        // restore to 1st savepoint
        savepoint1.restore();
        Assertions.assertEquals(name1, account.getOwnerName());
        Assertions.assertTrue(currencies1.equals(account.getCurrencies()));

        // restore to 2st savepoint
        savepoint2.restore();
        Assertions.assertEquals(name2, account.getOwnerName());
        Assertions.assertTrue(currencies2.equals(account.getCurrencies()));
    }
}
