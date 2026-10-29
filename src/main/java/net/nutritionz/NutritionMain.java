package net.nutritionz;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.nutritionz.init.CommandInit;
import net.nutritionz.init.ConfigInit;
import net.nutritionz.init.EventInit;
import net.nutritionz.init.LoaderInit;
import net.nutritionz.network.NutritionServerPacket;

public class NutritionMain implements ModInitializer {

    public static final Map<Item, List<Integer>> NUTRITION_ITEM_MAP = new ConcurrentHashMap<>();
    public static final Map<Integer, List<Object>> NUTRITION_POSITIVE_EFFECTS = new ConcurrentHashMap<>();
    public static final Map<Integer, List<Object>> NUTRITION_NEGATIVE_EFFECTS = new ConcurrentHashMap<>();

    @Override
    public void onInitialize() {
        ConfigInit.init();
        LoaderInit.init();
        NutritionServerPacket.init();
        EventInit.init();
        CommandInit.init();
    }

}
