package net.nutritionz.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {

    @Inject(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"))
    private static void tryEatMixin(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<ActionResult> info) {
        if (!world.isClient()) {
            ItemStack stack = new ItemStack(state.getBlock().asItem());
            if (NutritionMain.NUTRITION_ITEM_MAP.containsKey(stack.getItem())) {
                for (int i = 0; i < NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem()).size(); i++) {
                    if (NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem()).get(i) > 0) {
                        ((HungerManagerAccess) player.getHungerManager()).addNutritionLevel(i, NutritionMain.NUTRITION_ITEM_MAP.get(stack.getItem()).get(i));
                    }
                }
            }
        }
    }
}
