
package waypoint.model;

import java.util.Currency;
import java.util.Objects;

/**
 * VALUE OBJECT.
 * Represents exact money using paise.
 */
public final class Money implements Comparable<Money> {

    private final long paise;
    private final Currency currency;

    // Private constructor
    private Money(long paise, Currency currency) {
        this.paise = paise;
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
    }

    // Factory method using minor units (paise)
    public static Money ofMinor(long paise, Currency currency) {
        return new Money(paise, currency);
    }

    // Factory method using rupees
    public static Money rupees(long rupees) {
        return new Money(Math.multiplyExact(rupees, 100L),
                Currency.getInstance("INR"));
    }

    // Addition
    public Money plus(Money other) {
        checkCurrency(other);
        return new Money(Math.addExact(this.paise, other.paise), currency);
    }

    // Subtraction
    public Money minus(Money other) {
        checkCurrency(other);
        return new Money(Math.subtractExact(this.paise, other.paise), currency);
    }

    // Multiplication
    public Money times(int multiplier) {
        return new Money(Math.multiplyExact(this.paise, multiplier), currency);
    }

    // Check whether amount is negative
    public boolean isNegative() {
        return paise < 0;
    }

    // Compare two Money objects
    @Override
    public int compareTo(Money other) {
        checkCurrency(other);
        return Long.compare(this.paise, other.paise);
    }

    // Allocate money equally among n shares
    public Money[] allocate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of shares must be positive");
        }

        Money[] shares = new Money[n];

        long quotient = paise / n;
        long remainder = paise % n;

        for (int i = 0; i < n; i++) {
            long share = quotient;

            if (i < Math.abs(remainder)) {
                share += (paise >= 0) ? 1 : -1;
            }

            shares[i] = new Money(share, currency);
        }

        return shares;
    }

    // Check currency compatibility
    private void checkCurrency(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");

        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies do not match");
        }
    }

    // Equality
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Money)) return false;

        Money other = (Money) obj;

        return paise == other.paise &&
                currency.equals(other.currency);
    }

    // Hash code
    @Override
    public int hashCode() {
        return Objects.hash(paise, currency);
    }

    // String representation
    @Override
    public String toString() {
        return currency.getCurrencyCode() + " " +
                (paise / 100) + "." +
                String.format("%02d", Math.abs(paise % 100));
    }
}