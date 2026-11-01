package net.nutritionz.data;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.nutritionz.NutritionMain;
import net.nutritionz.api.NutritionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class NutritionLoader implements SimpleSynchronousResourceReloadListener {

    public static final Logger LOGGER = LogManager.getLogger("NutritionZ");

    @Override
    public ResourceLocation getFabricId() {
        return ResourceLocation.fromNamespaceAndPath("nutritionz", "loader");
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        Map<Item, List<Integer>> itemMap = new HashMap<>();
        Set<Item> replacedItems = new HashSet<>();
        Map<Integer, List<Object>> positiveEffects = new HashMap<>();
        Map<Integer, List<Object>> negativeEffects = new HashMap<>();
        boolean[] replacedEffects = new boolean[NutritionType.values().length];

        manager.listResources("nutrition", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try (InputStream stream = resourceRef.open()) {
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                for (String itemId : data.keySet()) {
                    try {
                        ResourceLocation itemIdentifier = ResourceLocation.tryParse(itemId);
                        if (itemIdentifier == null || !BuiltInRegistries.ITEM.containsKey(itemIdentifier)) {
                            LOGGER.info("{} is not a valid item identifier", itemId);
                            continue;
                        }

                        Item item = BuiltInRegistries.ITEM.get(itemIdentifier);
                        if (replacedItems.contains(item)) {
                            continue;
                        }
                        JsonObject jsonObject = data.get(itemId).getAsJsonObject();
                        if (GsonHelper.getAsBoolean(jsonObject, "replace", false)) {
                            replacedItems.add(item);
                        }
                        List<Integer> nutritionList = new ArrayList<>();
                        for (NutritionType type : NutritionType.values()) {
                            nutritionList.add(GsonHelper.getAsInt(jsonObject, type.getId(), 0));
                        }
                        itemMap.put(item, nutritionList);
                    } catch (Exception e) {
                        LOGGER.error("Error occurred while loading {} in resource {}. {}", itemId, id.toString(), e.toString());
                    }
                }

            } catch (Exception e) {
                LOGGER.error("Error occurred while loading resource {}. {}", id.toString(), e.toString());
            }
        });

        manager.listResources("nutrition_manager", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try (InputStream stream = resourceRef.open()) {
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                for (NutritionType type : NutritionType.values()) {
                    int i = type.getIndex();
                    if (!data.has(type.getId()) || replacedEffects[i]) {
                        continue;
                    }
                    JsonObject jsonObject = data.getAsJsonObject(type.getId());

                    if (GsonHelper.getAsBoolean(jsonObject, "replace", false)) {
                        replacedEffects[i] = true;
                        positiveEffects.remove(i);
                        negativeEffects.remove(i);
                    }

                    if (jsonObject.has("positive")) {
                        processEffects(jsonObject.getAsJsonObject("positive"), positiveEffects.computeIfAbsent(i, key -> new ArrayList<>()), type, true, id);
                    }
                    if (jsonObject.has("negative")) {
                        processEffects(jsonObject.getAsJsonObject("negative"), negativeEffects.computeIfAbsent(i, key -> new ArrayList<>()), type, false, id);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred while loading resource {}. {}", id.toString(), e.toString());
            }
        });

        NutritionMain.NUTRITION_ITEM_MAP.clear();
        NutritionMain.NUTRITION_ITEM_MAP.putAll(itemMap);
        NutritionMain.NUTRITION_POSITIVE_EFFECTS.clear();
        NutritionMain.NUTRITION_POSITIVE_EFFECTS.putAll(positiveEffects);
        NutritionMain.NUTRITION_NEGATIVE_EFFECTS.clear();
        NutritionMain.NUTRITION_NEGATIVE_EFFECTS.putAll(negativeEffects);
    }

    public static ResourceLocation getModifierId(NutritionType type, boolean positive, ResourceLocation attributeId) {
        return ResourceLocation.fromNamespaceAndPath("nutritionz", type.getId() + "/" + (positive ? "positive" : "negative") + "/" + attributeId.getNamespace() + "/" + attributeId.getPath());
    }

    private static void processEffects(JsonObject effectsJsonObject, List<Object> list, NutritionType type, boolean positive, ResourceLocation resourceId) {
        for (String effectId : effectsJsonObject.keySet()) {
            try {
                ResourceLocation effectIdentifier = ResourceLocation.parse(effectId);
                JsonObject effectJsonObject = effectsJsonObject.get(effectId).getAsJsonObject();

                if (BuiltInRegistries.MOB_EFFECT.containsKey(effectIdentifier)) {
                    Holder<MobEffect> statusEffect = BuiltInRegistries.MOB_EFFECT.getHolder(effectIdentifier).get();
                    list.removeIf(object -> object instanceof MobEffectInstance statusEffectInstance && statusEffectInstance.getEffect().equals(statusEffect));
                    list.add(new MobEffectInstance(statusEffect, GsonHelper.getAsInt(effectJsonObject, "duration"), GsonHelper.getAsInt(effectJsonObject, "amplifier", 0), false, false, true));
                } else if (BuiltInRegistries.ATTRIBUTE.containsKey(effectIdentifier)) {
                    Holder<Attribute> attribute = BuiltInRegistries.ATTRIBUTE.getHolder(effectIdentifier).get();
                    list.removeIf(object -> object instanceof Multimap<?, ?> multimap && multimap.containsKey(attribute));
                    Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                    attributeModifiers.put(attribute, new AttributeModifier(getModifierId(type, positive, effectIdentifier), GsonHelper.getAsFloat(effectJsonObject, "value"),
                            Operation.valueOf(GsonHelper.getAsString(effectJsonObject, "operation").toUpperCase())));
                    list.add(attributeModifiers);
                } else {
                    LOGGER.info("{} is not a valid status effect identifier nor attribute identifier", effectIdentifier);
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred while loading effect {} in resource {}. {}", effectId, resourceId.toString(), e.toString());
            }
        }
    }

}
