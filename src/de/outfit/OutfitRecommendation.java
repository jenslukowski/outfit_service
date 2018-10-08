package de.outfit;

public enum OutfitRecommendation {
    veryLight(1),
    light(2),
    normal(3),
    warm(4),
    veryWarm(5);

    private final int level;

    OutfitRecommendation(int level) {
        this.level = level;
    }

    public static OutfitRecommendation recommendOutfitFor(Temperature temperature) {
        return veryLight;
    }

    public int getLevel() {
        return level;
    }
}
