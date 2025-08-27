package net.nutritionz.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.nutritionz.util.NutritionUtil;

@Environment(EnvType.CLIENT)
public class ItemStackTooltipInit {

    public static void init() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
            NutritionUtil.addNutritionToolTip(itemStack, list);
        });
    }

}
