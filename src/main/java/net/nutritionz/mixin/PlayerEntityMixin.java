package net.nutritionz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {// implements PlayerNutritionAccess

    // private NutritionManager nutritionManager = new NutritionManager();

    // @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    // private void readCustomDataFromNbtMixin(NbtCompound nbt, CallbackInfo info) {
    // nutritionManager.readNbt(nbt);
    // }

    // @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    // private void writeCustomDataToNbtMixin(NbtCompound nbt, CallbackInfo info) {
    // nutritionManager.writeNbt(nbt);
    // }

    // @Override
    // public NutritionManager getNutritionManager() {
    // return this.nutritionManager;
    // }
}
