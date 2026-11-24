
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import waypoint.model.Spot;
import waypoint.model.SpotId;
import waypoint.model.VehicleId;

class SpotTest {

    @Test
    void aSpotCannotBeAssignedTwice() {

        Spot s = new Spot(new SpotId("S1"));

        assertTrue(
                s.tryAssign(new VehicleId("V1"))
        );

        assertFalse(
                s.tryAssign(new VehicleId("V2")),
                "Second claim must fail, not overwrite"
        );
    }

    @Test
    void releaseIsIdempotent() {

        Spot s = new Spot(new SpotId("S1"));

        // Never assigned — must not throw
        s.release(new VehicleId("V1"));

        assertTrue(s.isFree());
    }

    @Test
    void spotIdentityIsItsIdNotItsState() {

        Spot a = new Spot(new SpotId("S1"));
        Spot b = new Spot(new SpotId("S1"));

        a.tryAssign(new VehicleId("V1"));

        assertEquals(
                a,
                b,
                "An entity stays the same entity when its state changes"
        );
    }
}