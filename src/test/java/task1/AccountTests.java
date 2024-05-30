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

}
