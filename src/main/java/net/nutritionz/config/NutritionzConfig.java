package net.nutritionz.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "nutritionz")
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class NutritionzConfig implements ConfigData {

    public int posX = 162;
    public int posY = 5;
    @Comment("Used for the nutrition gui")
    public String carbohydrateItemId = "minecraft:sugar";
    public String proteinItemId = "minecraft:chicken";
    public String fatItemId = "minecraft:porkchop";
    public String vitaminItemId = "minecraft:apple";
}
