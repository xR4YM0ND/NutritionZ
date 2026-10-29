package net.nutritionz.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public final class NutritionEvents {

    public static final Event<ItemNutrition> ITEM_NUTRITION = EventFactory.createArrayBacked(ItemNutrition.class, listeners -> (player, stack, type, value) -> {
        for (ItemNutrition listener : listeners) {
            value = listener.modifyNutrition(player, stack, type, value);
        }
        return value;
    });

    public static final Event<NutritionChanged> NUTRITION_CHANGED = EventFactory.createArrayBacked(NutritionChanged.class, listeners -> (player, type, oldLevel, newLevel) -> {
        for (NutritionChanged listener : listeners) {
            listener.onNutritionChanged(player, type, oldLevel, newLevel);
        }
    });

    private NutritionEvents() {
    }

    @FunctionalInterface
    public interface ItemNutrition {
        int modifyNutrition(PlayerEntity player, ItemStack stack, NutritionType type, int value);
    }

    @FunctionalInterface
    public interface NutritionChanged {
        void onNutritionChanged(PlayerEntity player, NutritionType type, int oldLevel, int newLevel);
    }
}
