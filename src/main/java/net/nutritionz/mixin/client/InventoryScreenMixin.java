package net.nutritionz.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.nutritionz.init.ConfigInit;
import net.nutritionz.init.RenderInit;
import net.nutritionz.network.NutritionClientPacket;
import net.nutritionz.screen.NutritionScreen;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {

    public InventoryScreenMixin(InventoryMenu screenHandler, Inventory playerInventory, Component text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClickedMixin(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> info) {
        if (this.minecraft != null && this.hoveredSlot == null && this.isHovering(ConfigInit.CONFIG.posX, ConfigInit.CONFIG.posY, 9, 9, (double) mouseX, (double) mouseY)) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            NutritionClientPacket.writeC2SNutritionPacket();
            this.minecraft.setScreen(new NutritionScreen(Component.translatable("screen.nutritionz")));
            info.setReturnValue(true);
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    protected void drawBackgroundMixin(GuiGraphics context, float delta, int mouseX, int mouseY, CallbackInfo info) {
        if (this.isHovering(ConfigInit.CONFIG.posX, ConfigInit.CONFIG.posY, 9, 9, (double) mouseX, (double) mouseY)) {
            context.blit(RenderInit.NUTRITION_ICONS, this.leftPos + ConfigInit.CONFIG.posX, this.topPos + ConfigInit.CONFIG.posY, 185, 10, 9, 9);
            context.renderTooltip(this.font, Component.translatable("screen.nutritionz"), mouseX, mouseY);
        } else {
            context.blit(RenderInit.NUTRITION_ICONS, this.leftPos + ConfigInit.CONFIG.posX, this.topPos + ConfigInit.CONFIG.posY, 176, 10, 9, 9);
        }
    }

}
