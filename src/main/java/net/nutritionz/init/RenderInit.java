package net.nutritionz.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RenderInit {

    public static final Identifier NUTRITION_ICONS = Identifier.of("nutritionz", "textures/gui/icons.png");

    public static void init() {
    }

}
