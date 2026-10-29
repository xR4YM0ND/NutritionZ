package net.nutritionz.data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.registry.entry.RegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.nutritionz.NutritionMain;
import net.nutritionz.api.NutritionType;

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
            try {
                InputStream stream = resourceRef.getInputStream();
                JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                for (String itemId : data.keySet()) {
                    if (Registries.ITEM.get(Identifier.of(itemId)).toString().equals("air")) {
                        LOGGER.info("{} is not a valid item identifier", itemId);
                        continue;
                    }

                    JsonObject jsonObject = data.get(itemId).getAsJsonObject();
                    Item item = Registries.ITEM.get(Identifier.of(itemId));
                    if (replaceList.containsKey(item)) {
                        continue;
                    }
                    if (jsonObject.has("replace") && jsonObject.get("replace").getAsBoolean()) {
                        replaceList.put(item, true);
                    }
                    List<Integer> nutritionList = new ArrayList<>();
                    if (jsonObject.has("carbohydrates")) {
                        nutritionList.add(jsonObject.get("carbohydrates").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    if (jsonObject.has("protein")) {
                        nutritionList.add(jsonObject.get("protein").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    if (jsonObject.has("fat")) {
                        nutritionList.add(jsonObject.get("fat").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    if (jsonObject.has("vitamins")) {
                        nutritionList.add(jsonObject.get("vitamins").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    if (jsonObject.has("minerals")) {
                        nutritionList.add(jsonObject.get("minerals").getAsInt());
                    } else {
                        nutritionList.add(0);
                    }
                    NutritionMain.NUTRITION_ITEM_MAP.put(item, nutritionList);
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

    private static void processEffects(JsonObject effectsJsonObject, HashMap<Integer, List<Object>> nutritionEffectsMap, int i) {
        List<Object> list = new ArrayList<Object>();

        for (String effectId : effectsJsonObject.keySet()) {
            Identifier effectIdentifier = Identifier.of(effectId);

            if (!Registries.STATUS_EFFECT.containsId(effectIdentifier) && !Registries.ATTRIBUTE.containsId(effectIdentifier)) {
                LOGGER.info("{} is not a valid status effect identifier nor attribute identifier", effectIdentifier);
                continue;
            }

            JsonObject effectJsonObject = effectsJsonObject.get(effectId).getAsJsonObject();
            if (Registries.STATUS_EFFECT.containsId(effectIdentifier)) {
                list.add(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(effectIdentifier).get(), effectJsonObject.get("duration").getAsInt(),
                        effectJsonObject.has("amplifier") ? effectJsonObject.get("amplifier").getAsInt() : 0, false, false, true));
            } else {
                Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifiers = LinkedHashMultimap.create();
                attributeModifiers.put(Registries.ATTRIBUTE.getEntry(effectIdentifier).get(), new EntityAttributeModifier(effectIdentifier,
                        effectJsonObject.get("value").getAsFloat(), Operation.valueOf(effectJsonObject.get("operation").getAsString().toUpperCase())));
                list.add(attributeModifiers);
            }
        }

        nutritionEffectsMap.put(i, list);
    }

}
