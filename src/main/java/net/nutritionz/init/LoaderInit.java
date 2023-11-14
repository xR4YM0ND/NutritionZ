package net.nutritionz.init;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.nutritionz.data.NutritionLoader;

public class LoaderInit {

    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new NutritionLoader());
    }
}
