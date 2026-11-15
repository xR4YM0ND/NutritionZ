package net.nutritionz.mixin.client;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.nutritionz.util.NutritionUtil;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", shift = At.Shift.AFTER))
    private void getTooltipMixin(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag type, Consumer<Component> consumer, CallbackInfo info) {
        if (player != null) {
            NutritionUtil.addNutritionToolTip((ItemStack) (Object) this, consumer);
        }
    }

}
