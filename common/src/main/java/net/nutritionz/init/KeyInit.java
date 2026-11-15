package net.nutritionz.init;

import net.nutritionz.network.NutritionClientPacket;
import net.nutritionz.screen.NutritionScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class KeyInit {
    public static final Identifier CATEGORY_ID = Identifier.fromNamespaceAndPath("nutritionz", "keybind");
    public static KeyMapping screenKey;

    public static KeyMapping createScreenKey(KeyMapping.Category category) {
        screenKey = new KeyMapping("key.nutritionz.opennutritionscreen", InputConstants.Type.KEYBOARD, InputConstants.UNKNOWN.getValue(), category);
        return screenKey;
    }

    public static void onClientTick(Minecraft client) {
        if (screenKey != null && screenKey.consumeClick()) {
            if (client.player != null && client.player.connection != null) {
                NutritionClientPacket.writeC2SNutritionPacket();
                client.gui.setScreen(new NutritionScreen(Component.translatable("screen.nutritionz")));
                return;
            }
        }
    }

}
