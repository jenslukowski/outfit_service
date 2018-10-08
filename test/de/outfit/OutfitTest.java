package de.outfit;

import org.junit.Test;

import static de.outfit.OutfitRecommendation.veryLight;
import static org.junit.Assert.assertEquals;

public class OutfitTest {

    @Test
    public void temperatureGreaterThan26ShouldResultInVeryLight() {
        assertEquals(veryLight, OutfitRecommendation.recommendOutfitFor(new Temperature(27)));
    }
}
