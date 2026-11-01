package net.nutritionz.mixin;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.api.NutritionEvents;
import net.nutritionz.api.NutritionType;
import net.nutritionz.init.ConfigInit;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(FoodData.class)
public class HungerManagerMixin implements HungerManagerAccess {

    @Unique
    private int carbohydrateLevel = ConfigInit.CONFIG.maxNutrition / 2;
    @Unique
    private int proteinLevel = ConfigInit.CONFIG.maxNutrition / 2;
    @Unique
    private int fatLevel = ConfigInit.CONFIG.maxNutrition / 2;
    @Unique
    private int vitaminLevel = ConfigInit.CONFIG.maxNutrition / 2;
    @Unique
    private int mineralLevel = ConfigInit.CONFIG.maxNutrition / 2;
    @Unique
    private final Map<Identifier, Holder<Attribute>> appliedModifiers = new HashMap<>();
    // Unused
    private boolean shouldUpdateNutritions = false;

    @Shadow
    private int tickTimer;

    @Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(II)I", ordinal = 0))
    private void updateNutritionMixin(ServerPlayer player, CallbackInfo info) {
        for (NutritionType type : NutritionType.values()) {
            int oldLevel = getNutritionLevel(type.getIndex());
            decrementNutritionLevel(type.getIndex(), 1);
            int newLevel = getNutritionLevel(type.getIndex());
            if (oldLevel != newLevel) {
                NutritionEvents.NUTRITION_CHANGED.invoker().onNutritionChanged(player, type, oldLevel, newLevel);
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void updateMixin(ServerPlayer player, CallbackInfo info) {
        if (this.tickTimer % 5 == 0 && this.shouldUpdateNutritions) {
            this.shouldUpdateNutritions = false;
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "tick", at = @At("TAIL"))
    private void updateNutritionEffectsMixin(ServerPlayer player, CallbackInfo info) {
        if (player.level().getGameTime() % 20 != 0) {
            return;
        }
        Multimap<Holder<Attribute>, AttributeModifier> activeModifiers = LinkedHashMultimap.create();
        if (!player.isCreative() && !player.isSpectator()) {
            for (int i = 0; i < NutritionType.values().length; i++) {
                List<Object> effectList = getActiveEffects(i);
                if (effectList == null) {
                    continue;
                }
                for (Object effect : effectList) {
                    if (effect instanceof MobEffectInstance statusEffectInstance) {
                        if (!player.hasEffect(statusEffectInstance.getEffect())
                                || player.getEffect(statusEffectInstance.getEffect()).getDuration() < statusEffectInstance.getDuration() - 50) {
                            player.addEffect(new MobEffectInstance(statusEffectInstance));
                        }
                    } else if (effect instanceof Multimap<?, ?> multimap) {
                        activeModifiers.putAll((Multimap<Holder<Attribute>, AttributeModifier>) multimap);
                    }
                }
            }
        }

        Iterator<Map.Entry<Identifier, Holder<Attribute>>> iterator = this.appliedModifiers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Identifier, Holder<Attribute>> entry = iterator.next();
            if (activeModifiers.get(entry.getValue()).stream().noneMatch(modifier -> modifier.id().equals(entry.getKey()))) {
                AttributeInstance attributeInstance = player.getAttributes().getInstance(entry.getValue());
                if (attributeInstance != null) {
                    attributeInstance.removeModifier(entry.getKey());
                }
                iterator.remove();
            }
        }

        activeModifiers.forEach((attribute, modifier) -> {
            AttributeInstance attributeInstance = player.getAttributes().getInstance(attribute);
            if (attributeInstance != null) {
                AttributeModifier currentModifier = attributeInstance.getModifier(modifier.id());
                if (!modifier.equals(currentModifier)) {
                    if (currentModifier != null) {
                        attributeInstance.removeModifier(modifier.id());
                    }
                    attributeInstance.addTransientModifier(modifier);
                }
                this.appliedModifiers.put(modifier.id(), attribute);
            }
        });
    }

    @Unique
    @Nullable
    private List<Object> getActiveEffects(int type) {
        int level = getNutritionLevel(type);
        if (level <= ConfigInit.CONFIG.negativeNutrition) {
            return NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(type);
        } else if (level >= ConfigInit.CONFIG.positiveNutrition) {
            return NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(type);
        }
        return null;
    }

    @Inject(method = "addExhaustion", at = @At("TAIL"))
    private void addExhaustionMixin(float exhaustion, CallbackInfo info) {
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNbtMixin(ValueInput nbt, CallbackInfo info) {
        this.carbohydrateLevel = readNutritionLevel(nbt, "CarbohydrateLevel", this.carbohydrateLevel);
        this.proteinLevel = readNutritionLevel(nbt, "ProteinLevel", this.proteinLevel);
        this.fatLevel = readNutritionLevel(nbt, "FatLevel", this.fatLevel);
        this.vitaminLevel = readNutritionLevel(nbt, "VitaminLevel", this.vitaminLevel);
        this.mineralLevel = readNutritionLevel(nbt, "MineralLevel", this.mineralLevel);
    }

    @Unique
    private static int readNutritionLevel(ValueInput nbt, String key, int defaultLevel) {
        return Mth.clamp(nbt.getIntOr(key, defaultLevel), 0, ConfigInit.CONFIG.maxNutrition);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeNbtMixin(ValueOutput nbt, CallbackInfo info) {
        nbt.putInt("CarbohydrateLevel", this.carbohydrateLevel);
        nbt.putInt("ProteinLevel", this.proteinLevel);
        nbt.putInt("FatLevel", this.fatLevel);
        nbt.putInt("VitaminLevel", this.vitaminLevel);
        nbt.putInt("MineralLevel", this.mineralLevel);
    }

    @Override
    public void addNutritionLevel(int type, int level) {
        if (type == 0) {
            this.carbohydrateLevel = Math.min(this.carbohydrateLevel + level, ConfigInit.CONFIG.maxNutrition);
        } else if (type == 1) {
            this.proteinLevel = Math.min(this.proteinLevel + level, ConfigInit.CONFIG.maxNutrition);
        } else if (type == 2) {
            this.fatLevel = Math.min(this.fatLevel + level, ConfigInit.CONFIG.maxNutrition);
        } else if (type == 3) {
            this.vitaminLevel = Math.min(this.vitaminLevel + level, ConfigInit.CONFIG.maxNutrition);
        } else if (type == 4) {
            this.mineralLevel = Math.min(this.mineralLevel + level, ConfigInit.CONFIG.maxNutrition);
        }
        this.shouldUpdateNutritions = true;
    }

    @Override
    public void decrementNutritionLevel(int type, int level) {
        if (type == 0) {
            this.carbohydrateLevel = Math.max(this.carbohydrateLevel - level, 0);
        } else if (type == 1) {
            this.proteinLevel = Math.max(this.proteinLevel - level, 0);
        } else if (type == 2) {
            this.fatLevel = Math.max(this.fatLevel - level, 0);
        } else if (type == 3) {
            this.vitaminLevel = Math.max(this.vitaminLevel - level, 0);
        } else if (type == 4) {
            this.mineralLevel = Math.max(this.mineralLevel - level, 0);
        }
        this.shouldUpdateNutritions = true;
    }

    @Override
    public void setNutritionLevel(int type, int level) {
        if (type == 0) {
            this.carbohydrateLevel = level;
        } else if (type == 1) {
            this.proteinLevel = level;
        } else if (type == 2) {
            this.fatLevel = level;
        } else if (type == 3) {
            this.vitaminLevel = level;
        } else if (type == 4) {
            this.mineralLevel = level;
        }
        this.shouldUpdateNutritions = true;
    }

    @Override
    public int getNutritionLevel(int type) {
        return switch (type) {
            case 0 -> this.carbohydrateLevel;
            case 1 -> this.proteinLevel;
            case 2 -> this.fatLevel;
            case 3 -> this.vitaminLevel;
            case 4 -> this.mineralLevel;
            default -> 0;
        };
    }

}
