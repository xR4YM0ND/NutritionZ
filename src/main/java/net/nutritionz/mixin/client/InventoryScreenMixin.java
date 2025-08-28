package net.nutritionz.mixin.client;

import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.text.Text;
import net.nutritionz.init.ConfigInit;
import net.nutritionz.init.RenderInit;
import net.nutritionz.network.NutritionClientPacket;
import net.nutritionz.screen.NutritionScreen;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin<T extends AbstractRecipeScreenHandler> extends HandledScreen<T> {

    public InventoryScreenMixin(T screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void mouseClickedMixin(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> info) {
        if (this.client != null && this.focusedSlot == null && this.isPointWithinBounds(ConfigInit.CONFIG.posX,
                ConfigInit.CONFIG.posY, 9, 9, (double) mouseX, (double) mouseY)) {
            this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            NutritionClientPacket.writeC2SNutritionPacket();
            this.client.setScreen(new NutritionScreen(Text.translatable("screen.nutritionz")));
            info.setReturnValue(true);
        }
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    protected void drawBackgroundMixin(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
        if (this.isPointWithinBounds(ConfigInit.CONFIG.posX, ConfigInit.CONFIG.posY, 9, 9, (double) mouseX,
                (double) mouseY)) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, RenderInit.NUTRITION_ICONS,
                    this.x + ConfigInit.CONFIG.posX,
                    this.y + ConfigInit.CONFIG.posY, 185, 10, 9, 9, 256, 256);
            context.drawTooltip(this.textRenderer, Text.translatable("screen.nutritionz"), mouseX, mouseY);
        } else {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, RenderInit.NUTRITION_ICONS,
                    this.x + ConfigInit.CONFIG.posX,
                    this.y + ConfigInit.CONFIG.posY, 176, 10, 9, 9, 256, 256);
        }
    }

}
