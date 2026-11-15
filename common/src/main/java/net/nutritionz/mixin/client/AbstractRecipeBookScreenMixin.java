package net.nutritionz.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.nutritionz.init.ConfigInit;
import net.nutritionz.network.NutritionClientPacket;
import net.nutritionz.screen.NutritionScreen;

@Mixin(AbstractRecipeBookScreen.class)
public abstract class AbstractRecipeBookScreenMixin<T extends RecipeBookMenu> extends AbstractContainerScreen<T> {

    public AbstractRecipeBookScreenMixin(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClickedMixin(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> info) {
        if ((Object) this instanceof InventoryScreen && this.minecraft != null && this.hoveredSlot == null
                && this.isHovering(ConfigInit.CONFIG.posX, ConfigInit.CONFIG.posY, 9, 9, event.x(), event.y())) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            NutritionClientPacket.writeC2SNutritionPacket();
            this.minecraft.gui.setScreen(new NutritionScreen(Component.translatable("screen.nutritionz")));
            info.setReturnValue(true);
        }
    }

}
