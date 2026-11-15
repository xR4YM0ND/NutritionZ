package net.nutritionz;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.item.Item;
import net.nutritionz.init.ConfigInit;

public class NutritionMain {

    public static final String MOD_ID = "nutritionz";

    public static final Map<Item, List<Integer>> NUTRITION_ITEM_MAP = new ConcurrentHashMap<>();
    public static final Map<Integer, List<Object>> NUTRITION_POSITIVE_EFFECTS = new ConcurrentHashMap<>();
    public static final Map<Integer, List<Object>> NUTRITION_NEGATIVE_EFFECTS = new ConcurrentHashMap<>();

    public static void init() {
        ConfigInit.init();
    }

}
