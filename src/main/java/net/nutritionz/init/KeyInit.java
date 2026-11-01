package net.nutritionz.init;

import net.nutritionz.network.NutritionClientPacket;
import net.nutritionz.screen.NutritionScreen;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class KeyInit {
    public static final KeyMapping screenKey = new KeyMapping("key.nutritionz.opennutritionscreen", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.nutritionz.keybind");

    public static void init() {
        // Registering
        KeyBindingHelper.registerKeyBinding(screenKey);
        // Callback
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (screenKey.consumeClick()) {
                if (client.player != null && client.player.connection != null) {
                    NutritionClientPacket.writeC2SNutritionPacket();
                    client.setScreen(new NutritionScreen(Component.translatable("screen.nutritionz")));
                    return;
                }
            }
        });
    }

}
