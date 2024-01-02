package net.nutritionz;

import java.util.HashMap;
import java.util.List;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.nutritionz.init.ConfigInit;
import net.nutritionz.init.EventInit;
import net.nutritionz.init.LoaderInit;
import net.nutritionz.network.NutritionServerPacket;

public class NutritionMain implements ModInitializer {

    public static final HashMap<Item, List<Integer>> NUTRITION_ITEM_MAP = new HashMap<Item, List<Integer>>();
    public static final HashMap<Integer, List<Object>> NUTRITION_POSITIVE_EFFECTS = new HashMap<Integer, List<Object>>();
    public static final HashMap<Integer, List<Object>> NUTRITION_NEGATIVE_EFFECTS = new HashMap<Integer, List<Object>>();

    @Override
    public void onInitialize() {
        ConfigInit.init();
        LoaderInit.init();
        NutritionServerPacket.init();
        EventInit.init();
    }

}
