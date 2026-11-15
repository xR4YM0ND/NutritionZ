package net.nutritionz.fabric.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.nutritionz.platform.services.IPlatformHelper;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

}
