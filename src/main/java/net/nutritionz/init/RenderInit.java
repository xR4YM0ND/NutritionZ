package net.nutritionz.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class RenderInit {

    public static final ResourceLocation NUTRITION_ICONS = ResourceLocation.fromNamespaceAndPath("nutritionz", "textures/gui/icons.png");

    public static void init() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
        });
    }

}
