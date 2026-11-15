package net.nutritionz.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.nutritionz.init.ConfigInit;
import net.nutritionz.init.RenderInit;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> {

    public InventoryScreenMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component title) {
        super(menu, recipeBookComponent, inventory, title);
    }

    @Inject(method = "extractBackground", at = @At("TAIL"))
    protected void drawBackgroundMixin(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (this.isHovering(ConfigInit.CONFIG.posX, ConfigInit.CONFIG.posY, 9, 9, (double) mouseX, (double) mouseY)) {
            context.blit(RenderPipelines.GUI_TEXTURED, RenderInit.NUTRITION_ICONS, this.leftPos + ConfigInit.CONFIG.posX, this.topPos + ConfigInit.CONFIG.posY, 185, 10, 9, 9, 256, 256);
            context.setTooltipForNextFrame(this.font, Component.translatable("screen.nutritionz"), mouseX, mouseY);
        } else {
            context.blit(RenderPipelines.GUI_TEXTURED, RenderInit.NUTRITION_ICONS, this.leftPos + ConfigInit.CONFIG.posX, this.topPos + ConfigInit.CONFIG.posY, 176, 10, 9, 9, 256, 256);
        }
    }

}
