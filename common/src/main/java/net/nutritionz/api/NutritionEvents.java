package net.nutritionz.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.nutritionz.api.event.Event;

public final class NutritionEvents {

    public static final Event<ItemNutrition> ITEM_NUTRITION = Event.create(ItemNutrition.class, listeners -> (player, stack, type, value) -> {
        for (ItemNutrition listener : listeners) {
            value = listener.modifyNutrition(player, stack, type, value);
        }
        return value;
    });

    public static final Event<NutritionChanged> NUTRITION_CHANGED = Event.create(NutritionChanged.class, listeners -> (player, type, oldLevel, newLevel) -> {
        for (NutritionChanged listener : listeners) {
            listener.onNutritionChanged(player, type, oldLevel, newLevel);
        }
    });

    private NutritionEvents() {
    }

    @FunctionalInterface
    public interface ItemNutrition {
        int modifyNutrition(Player player, ItemStack stack, NutritionType type, int value);
    }

    @FunctionalInterface
    public interface NutritionChanged {
        void onNutritionChanged(Player player, NutritionType type, int oldLevel, int newLevel);
    }
}
