package net.nutritionz.init;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.nutritionz.config.NutritionzConfig;

public class ConfigInit {

    public static NutritionzConfig CONFIG = new NutritionzConfig();

    public static void init() {
        AutoConfig.register(NutritionzConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(NutritionzConfig.class).getConfig();
    }
}
