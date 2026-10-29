package net.nutritionz.data;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
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
    public Identifier getFabricId() {
        return Identifier.of("nutritionz", "loader");
    }

    @Override
    public void reload(ResourceManager manager) {
        Map<Item, List<Integer>> itemMap = new HashMap<>();
        Set<Item> replacedItems = new HashSet<>();
        Map<Integer, List<Object>> positiveEffects = new HashMap<>();
        Map<Integer, List<Object>> negativeEffects = new HashMap<>();
        boolean[] replacedEffects = new boolean[NutritionType.values().length];

        manager.findResources("nutrition", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try (InputStream stream = resourceRef.getInputStream()) {
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                for (String itemId : data.keySet()) {
                    try {
                        Identifier itemIdentifier = Identifier.tryParse(itemId);
                        if (itemIdentifier == null || !Registries.ITEM.containsId(itemIdentifier)) {
                            LOGGER.info("{} is not a valid item identifier", itemId);
                            continue;
                        }

                        Item item = Registries.ITEM.get(itemIdentifier);
                        if (replacedItems.contains(item)) {
                            continue;
                        }
                        JsonObject jsonObject = data.get(itemId).getAsJsonObject();
                        if (JsonHelper.getBoolean(jsonObject, "replace", false)) {
                            replacedItems.add(item);
                        }
                        List<Integer> nutritionList = new ArrayList<>();
                        for (NutritionType type : NutritionType.values()) {
                            nutritionList.add(JsonHelper.getInt(jsonObject, type.getId(), 0));
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

        manager.findResources("nutrition_manager", id -> id.getPath().endsWith(".json")).forEach((id, resourceRef) -> {
            try (InputStream stream = resourceRef.getInputStream()) {
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                for (NutritionType type : NutritionType.values()) {
                    int i = type.getIndex();
                    if (!data.has(type.getId()) || replacedEffects[i]) {
                        continue;
                    }
                    JsonObject jsonObject = data.getAsJsonObject(type.getId());

                    if (JsonHelper.getBoolean(jsonObject, "replace", false)) {
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

    public static Identifier getModifierId(NutritionType type, boolean positive, Identifier attributeId) {
        return Identifier.of("nutritionz", type.getId() + "/" + (positive ? "positive" : "negative") + "/" + attributeId.getNamespace() + "/" + attributeId.getPath());
    }

    private static void processEffects(JsonObject effectsJsonObject, List<Object> list, NutritionType type, boolean positive, Identifier resourceId) {
        for (String effectId : effectsJsonObject.keySet()) {
            try {
                Identifier effectIdentifier = Identifier.of(effectId);
                JsonObject effectJsonObject = effectsJsonObject.get(effectId).getAsJsonObject();

                if (Registries.STATUS_EFFECT.containsId(effectIdentifier)) {
                    RegistryEntry<StatusEffect> statusEffect = Registries.STATUS_EFFECT.getEntry(effectIdentifier).get();
                    list.removeIf(object -> object instanceof StatusEffectInstance statusEffectInstance && statusEffectInstance.getEffectType().equals(statusEffect));
                    list.add(new StatusEffectInstance(statusEffect, JsonHelper.getInt(effectJsonObject, "duration"), JsonHelper.getInt(effectJsonObject, "amplifier", 0), false, false, true));
                } else if (Registries.ATTRIBUTE.containsId(effectIdentifier)) {
                    RegistryEntry<EntityAttribute> attribute = Registries.ATTRIBUTE.getEntry(effectIdentifier).get();
                    list.removeIf(object -> object instanceof Multimap<?, ?> multimap && multimap.containsKey(attribute));
                    Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                    attributeModifiers.put(attribute, new EntityAttributeModifier(getModifierId(type, positive, effectIdentifier), JsonHelper.getFloat(effectJsonObject, "value"),
                            Operation.valueOf(JsonHelper.getString(effectJsonObject, "operation").toUpperCase())));
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
