package de.outfit;

public enum OutfitRecommendation {
    veryLight(1, new Temperature(26)),
    light(2, new Temperature(21)),
    normal(3, new Temperature(15)),
    warm(4, new Temperature(5)),
    veryWarm(5, new Temperature(-273.15));

    private final int level;
    private final Temperature minTemperature;

    OutfitRecommendation(int level, Temperature minTemperature) {
        this.level = level;
        this.minTemperature = minTemperature;
    }

    public static OutfitRecommendation recommendOutfitFor(Temperature temperature) {
        for (OutfitRecommendation recommendation : OutfitRecommendation.values()) {
            if (recommendation.minTemperature.getTemperatureInCelsius() < temperature.getTemperatureInCelsius()) {
                return recommendation;
            }
        }
        return veryLight;
    }

    public int getLevel() {
        return level;
    }
}
