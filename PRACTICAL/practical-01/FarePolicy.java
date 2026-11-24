
package waypoint.model;

import java.time.Instant;

@FunctionalInterface
public interface FarePolicy {

    /**
     * Calculates parking fare.
     *
     * @param entry Entry time
     * @param exit Exit time
     * @param spot Parking spot ID
     * @return Calculated parking fare
     */
    Money fareFor(Instant entry, Instant exit, SpotId spot);
}