package net.nutritionz.api;

import org.jetbrains.annotations.Nullable;

public enum NutritionType {
    CARBOHYDRATES("carbohydrates"),
    PROTEIN("protein"),
    FAT("fat"),
    VITAMINS("vitamins"),
    MINERALS("minerals");

    private final String id;

    NutritionType(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public int getIndex() {
        return this.ordinal();
    }

    public String getTranslationKey() {
        return "screen.nutritionz." + this.id;
    }

    @Nullable
    public static NutritionType byId(String id) {
        for (NutritionType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return null;
    }

    public static NutritionType byIndex(int index) {
        return values()[index];
    }
}
