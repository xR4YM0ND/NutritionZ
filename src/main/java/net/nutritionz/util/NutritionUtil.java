package net.nutritionz.util;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.nutritionz.NutritionMain;

public class NutritionUtil {

    private static final List<String> NUTRITION_TOOLTIPS = List.of("item.nutritionz.carbohydrates", "item.nutritionz.protein", "item.nutritionz.fat", "item.nutritionz.vitamins",
            "item.nutritionz.minerals");

    public static void addNutritionToolTip(ItemStack stack, Consumer<Component> consumer) {
        if (Minecraft.getInstance().hasShiftDown() && NutritionMain.NUTRITION_ITEM_MAP.containsKey(stack.getItem())) {
            List<Integer> nutritionList = NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem());

            boolean hasNutritions = false;
            for (int i = 0; i < nutritionList.size(); i++) {
                if (nutritionList.get(i) > 0) {
                    if (!hasNutritions) {
                        consumer.accept(CommonComponents.EMPTY);
                        consumer.accept(Component.translatable("item.nutritionz.nutrients"));
                    }
                    consumer.accept(Component.translatable(NUTRITION_TOOLTIPS.get(i), nutritionList.get(i)).withStyle(ChatFormatting.GREEN));
                    hasNutritions = true;
                }
            }
        }
    }

}
