package net.nutritionz.init.dehydration;

import net.dehydration.api.DrinkEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;

public class DehydrationEventInit {
    public static void init() {
        DrinkEvent.EVENT.register((ItemStack stack, PlayerEntity player) -> {
            if (!player.getWorld().isClient()) {
                Item item = stack.getItem();
                if (NutritionMain.NUTRITION_ITEM_MAP.containsKey(item)) {
                    for (int i = 0; i < NutritionMain.NUTRITION_ITEM_MAP.get(item).size(); i++) {
                        if (NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i) > 0) {
                            ((HungerManagerAccess) player.getHungerManager()).addNutritionLevel(i,
                                    NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i));
                        }
                    }
                }
            }
        });
    }
}
