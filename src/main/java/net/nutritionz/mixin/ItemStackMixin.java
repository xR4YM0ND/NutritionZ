package net.nutritionz.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.nutritionz.api.NutritionApi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "finishUsing", at = @At("HEAD"))
    private void finishUsingMixin(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            NutritionApi.applyItemNutrition(serverPlayerEntity, (ItemStack) (Object) this);
        }
    }
}
