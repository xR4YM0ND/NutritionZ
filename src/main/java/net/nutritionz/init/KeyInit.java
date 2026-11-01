package net.nutritionz.init;

import net.nutritionz.network.NutritionClientPacket;
import net.nutritionz.screen.NutritionScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class KeyInit {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("nutritionz", "keybind"));
    public static final KeyMapping screenKey = new KeyMapping("key.nutritionz.opennutritionscreen", InputConstants.Type.KEYBOARD, InputConstants.UNKNOWN.getValue(), CATEGORY);

    public static void init() {
        // Registering
        KeyMappingHelper.registerKeyMapping(screenKey);
        // Callback
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (screenKey.consumeClick()) {
                if (client.player != null && client.player.connection != null) {
                    NutritionClientPacket.writeC2SNutritionPacket();
                    client.gui.setScreen(new NutritionScreen(Component.translatable("screen.nutritionz")));
                    return;
                }
            }
        });
    }

}
