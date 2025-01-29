package net.nutritionz.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "finishUsing", at = @At("HEAD"))
    private void finishUsingMixin(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            ItemStack stack = (ItemStack) (Object) this;
            if (NutritionMain.NUTRITION_ITEM_MAP.containsKey(stack.getItem())) {
                for (int i = 0; i < NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem()).size(); i++) {
                    if (NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem()).get(i) > 0) {
                        ((HungerManagerAccess) serverPlayerEntity.getHungerManager()).addNutritionLevel(i, NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem()).get(i));
                    }
                }
            }
        }
    }
}
