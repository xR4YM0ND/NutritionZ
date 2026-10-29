package net.nutritionz.api;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.init.ConfigInit;
import net.nutritionz.network.NutritionServerPacket;

public final class NutritionApi {

    private NutritionApi() {
    }

    public static int getNutrition(PlayerEntity player, NutritionType type) {
        return getAccess(player).getNutritionLevel(type.getIndex());
    }

    public static void setNutrition(PlayerEntity player, NutritionType type, int level) {
        if (changeNutrition(player, type, level) && player instanceof ServerPlayerEntity serverPlayerEntity) {
            syncNutrition(serverPlayerEntity);
        }
    }

    public static void addNutrition(PlayerEntity player, NutritionType type, int amount) {
        setNutrition(player, type, getNutrition(player, type) + amount);
    }

    public static void applyItemNutrition(PlayerEntity player, ItemStack stack) {
        List<Integer> nutritionList = NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem());
        boolean changed = false;
        for (NutritionType type : NutritionType.values()) {
            int value = NutritionEvents.ITEM_NUTRITION.invoker().modifyNutrition(player, stack, type, nutritionList != null ? nutritionList.get(type.getIndex()) : 0);
            if (value != 0) {
                changed |= changeNutrition(player, type, getNutrition(player, type) + value);
            }
        }
        if (changed && player instanceof ServerPlayerEntity serverPlayerEntity) {
            syncNutrition(serverPlayerEntity);
        }
    }

    public static boolean hasNutrition(Item item) {
        return NutritionMain.NUTRITION_ITEM_MAP.containsKey(item);
    }

    public static int getItemNutrition(Item item, NutritionType type) {
        List<Integer> nutritionList = NutritionMain.NUTRITION_ITEM_MAP.get(item);
        return nutritionList != null ? nutritionList.get(type.getIndex()) : 0;
    }

    public static int getMaxNutrition() {
        return ConfigInit.CONFIG.maxNutrition;
    }

    public static int getNegativeNutrition() {
        return ConfigInit.CONFIG.negativeNutrition;
    }

    public static int getPositiveNutrition() {
        return ConfigInit.CONFIG.positiveNutrition;
    }

    public static boolean isDeficient(PlayerEntity player, NutritionType type) {
        return getNutrition(player, type) <= getNegativeNutrition();
    }

    public static boolean isWellFed(PlayerEntity player, NutritionType type) {
        return !isDeficient(player, type) && getNutrition(player, type) >= getPositiveNutrition();
    }

    public static void syncNutrition(ServerPlayerEntity serverPlayerEntity) {
        NutritionServerPacket.writeS2CNutritionPacket(serverPlayerEntity, getAccess(serverPlayerEntity));
    }

    private static boolean changeNutrition(PlayerEntity player, NutritionType type, int level) {
        HungerManagerAccess hungerManagerAccess = getAccess(player);
        int oldLevel = hungerManagerAccess.getNutritionLevel(type.getIndex());
        int newLevel = MathHelper.clamp(level, 0, getMaxNutrition());
        if (oldLevel == newLevel) {
            return false;
        }
        hungerManagerAccess.setNutritionLevel(type.getIndex(), newLevel);
        NutritionEvents.NUTRITION_CHANGED.invoker().onNutritionChanged(player, type, oldLevel, newLevel);
        return true;
    }

    private static HungerManagerAccess getAccess(PlayerEntity player) {
        return (HungerManagerAccess) player.getHungerManager();
    }
}
