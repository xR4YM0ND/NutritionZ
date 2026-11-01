package net.nutritionz.util;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.nutritionz.NutritionMain;

public class NutritionUtil {

    private static final List<String> NUTRITION_TOOLTIPS = List.of("item.nutritionz.carbohydrates", "item.nutritionz.protein", "item.nutritionz.fat", "item.nutritionz.vitamins",
            "item.nutritionz.minerals");

    public static void addNutritionToolTip(ItemStack stack, List<Component> list) {
        if (Screen.hasShiftDown() && NutritionMain.NUTRITION_ITEM_MAP.containsKey(stack.getItem())) {
            List<Integer> nutritionList = NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem());

            boolean hasNutritions = false;
            for (int i = 0; i < nutritionList.size(); i++) {
                if (nutritionList.get(i) > 0) {
                    if (!hasNutritions) {
                        list.add(CommonComponents.EMPTY);
                        list.add(Component.translatable("item.nutritionz.nutrients"));
                    }
                    list.add(Component.translatable(NUTRITION_TOOLTIPS.get(i), nutritionList.get(i)).withStyle(ChatFormatting.GREEN));
                    hasNutritions = true;
                }
            }
        }
    }

}
