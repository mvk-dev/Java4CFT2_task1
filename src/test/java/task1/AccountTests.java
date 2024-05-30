package task1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTests {
    @Test
    void should_ThrowException_When_OwnerNameIsEmptyOrNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account(null));
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
}
