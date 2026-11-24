
package waypoint.model;

import java.util.*;

public final class Money implements Comparable<Money> {

    private final long paise;
    private final Currency currency;

    // Private constructor
    private Money(long paise, Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }

        this.paise = paise;
        this.currency = currency;
    }

    // Factory method: create Money using minor units
    public static Money ofMinor(long paise, Currency currency) {
        return new Money(paise, currency);
    }

    // Factory method: create Money in Indian Rupees
    public static Money rupees(long rupees) {
        return new Money(Math.multiplyExact(rupees, 100L),
                         Currency.getInstance("INR"));
    }

    // Addition
    public Money plus(Money other) {
        checkCurrency(other);
        return new Money(Math.addExact(this.paise, other.paise),
                         this.currency);
    }

    // Subtraction
    public Money minus(Money other) {
        checkCurrency(other);
        return new Money(Math.subtractExact(this.paise, other.paise),
                         this.currency);
    }

    // Multiplication
    public Money times(int multiplier) {
        return new Money(Math.multiplyExact(this.paise, (long) multiplier),
                         this.currency);
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

    // Allocate amount equally among n shares
    public List<Money> allocate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                "Number of shares must be positive");
        }

        List<Money> shares = new ArrayList<>();

        long quotient = paise / n;
        long remainder = paise % n;

        for (int i = 0; i < n; i++) {
            long share = quotient;

            if (i < Math.abs(remainder)) {
                share += (paise >= 0) ? 1 : -1;
            }

            shares.add(new Money(share, currency));
        }

        return Collections.unmodifiableList(shares);
    }

    // Check currency compatibility
    private void checkCurrency(Money other) {
        if (other == null || !this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "Currencies must be the same");
        }
    }

    // Equality check
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Money)) {
            return false;
        }

        Money other = (Money) obj;

        return this.paise == other.paise
                && this.currency.equals(other.currency);
    }

    // Hash code
    @Override
    public int hashCode() {
        return Objects.hash(paise, currency);
    }

    // String representation
    @Override
    public String toString() {
        long rupees = paise / 100;
        long remainingPaise = Math.abs(paise % 100);

        return currency.getCurrencyCode() + " "
                + rupees + "."
                + String.format("%02d", remainingPaise);
    }
}
