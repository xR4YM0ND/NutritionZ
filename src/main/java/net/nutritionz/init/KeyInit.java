package net.nutritionz.init;

import net.minecraft.text.Text;
import net.nutritionz.network.NutritionClientPacket;
import net.nutritionz.screen.NutritionScreen;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public class KeyInit {
    public static final KeyBinding screenKey = new KeyBinding("key.nutritionz.opennutritionscreen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.nutritionz.keybind");

    public static void init() {
        // Registering
        KeyBindingHelper.registerKeyBinding(screenKey);
        // Callback
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (screenKey.wasPressed()) {
                if (client.player != null && client.player.networkHandler != null) {
                    NutritionClientPacket.writeC2SNutritionPacket();
                    client.setScreen(new NutritionScreen(Text.translatable("screen.nutritionz")));
                    return;
                }
            }
        });
    }

}
