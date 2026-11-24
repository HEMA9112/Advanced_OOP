
package waypoint.model;

import java.time.Instant;
import java.util.Objects;

/**
 * ENTITY.
 * Invariants:
 * I1: Entry is always present.
 * I2: Exit is null, or not before entry.
 * I3: Fare is null, or not negative.
 * I4: Fare exists only if exit exists.
 * I5: A closed ticket never reopens.
 */
public final class Ticket {

    private final TicketId id;
    private final SpotId spotId;
    private final Instant entry;

    private Instant exit;   // Mutable field 1
    private Money fare;     // Mutable field 2

    // Private constructor
    private Ticket(TicketId id, SpotId spotId, Instant entry) {

        this.id = Objects.requireNonNull(id, "Ticket ID cannot be null");
        this.spotId = Objects.requireNonNull(spotId, "Spot ID cannot be null");
        this.entry = Objects.requireNonNull(entry, "Entry time cannot be null");

        this.exit = null;
        this.fare = null;
    }

    // Factory method to create an open ticket
    public static Ticket open(TicketId id, SpotId spotId, Instant entry) {
        return new Ticket(id, spotId, entry);
    }

    // Check whether ticket is open
    public boolean isOpen() {
        return exit == null;
    }

    // Close ticket and calculate fare
    public Money close(Instant at, FarePolicy policy) {

        Objects.requireNonNull(at, "Exit time cannot be null");
        Objects.requireNonNull(policy, "Fare policy cannot be null");

        // Reject second close
        if (!isOpen()) {
            throw new TicketAlreadyClosed();
        }

        // Exit cannot be before entry
        if (at.isBefore(entry)) {
            throw new IllegalArgumentException(
                    "Exit time cannot be before entry time"
            );
        }

        // Calculate fare before changing ticket state
        Money calculatedFare = Objects.requireNonNull(
                policy.calculate(entry, at),
                "Calculated fare cannot be null"
        );

        // Fare cannot be negative
        if (calculatedFare.isNegative()) {
            throw new IllegalArgumentException(
                    "Fare cannot be negative"
            );
        }

        // Update exit and fare together
        this.exit = at;
        this.fare = calculatedFare;

        return fare;
    }

    // Getters
    public TicketId id() {
        return id;
    }

    public SpotId spotId() {
        return spotId;
    }

    public Instant entry() {
        return entry;
    }

    public Instant exit() {
        return exit;
    }

    public Money fare() {
        return fare;
    }
}