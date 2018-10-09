package de.outfit;

import org.junit.Test;

import static de.outfit.OutfitRecommendation.*;
import static org.junit.Assert.assertEquals;

public class OutfitUnitTest {

    @Test
    public void temperatureGreaterThan26ShouldResultInVeryLight() {
        assertEquals(veryLight, OutfitRecommendation.recommendOutfitFor(new Temperature(27)));
    }

    @Test
    public void temperatureOf26ShouldResultInLight() {
        assertEquals(light, OutfitRecommendation.recommendOutfitFor(new Temperature(26)));
    }

    @Test
    public void temperatureOf24ShouldResultInLight() {
        assertEquals(light, OutfitRecommendation.recommendOutfitFor(new Temperature(24)));
    }

    @Test
    public void temperatureOf21ShouldResultInNormal() {
        assertEquals(normal, OutfitRecommendation.recommendOutfitFor(new Temperature(21)));
    }

    @Test
    public void temperatureOf15ShouldResultInWarm() {
        assertEquals(warm, OutfitRecommendation.recommendOutfitFor(new Temperature(15)));
    }

    @Test
    public void temperatureOf5ShouldResultInVeryWarm() {
        assertEquals(veryWarm, OutfitRecommendation.recommendOutfitFor(new Temperature(5)));
    }

    @Test
    public void temperatureLessThan5ShouldResultInVeryWarm() {
        assertEquals(veryWarm, OutfitRecommendation.recommendOutfitFor(new Temperature(-1)));
    }
}
