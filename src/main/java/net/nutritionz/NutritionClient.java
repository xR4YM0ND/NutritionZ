package net.nutritionz;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.nutritionz.init.RenderInit;
import net.nutritionz.network.NutritionClientPacket;

@Environment(EnvType.CLIENT)
public class NutritionClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RenderInit.init();
        NutritionClientPacket.init();
    }
}
