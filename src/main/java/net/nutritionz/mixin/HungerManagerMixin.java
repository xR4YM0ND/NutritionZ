package net.nutritionz.mixin;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;

@Mixin(HungerManager.class)
public class HungerManagerMixin implements HungerManagerAccess {

    private int carbohydrateLevel = NutritionMain.NUTRITION_MAX_VALUES;
    private int proteinLevel = NutritionMain.NUTRITION_MAX_VALUES;
    private int fatLevel = NutritionMain.NUTRITION_MAX_VALUES;
    private int vitaminLevel = NutritionMain.NUTRITION_MAX_VALUES;
    private Map<Integer, Boolean> effectMap = Map.of(0, false, 1, false, 2, false, 3, false);

    @Shadow
    private int foodTickTimer;

    @Inject(method = "eat", at = @At("TAIL"))
    private void eatMixin(Item item, ItemStack stack, CallbackInfo info) {
        if (NutritionMain.NUTRITION_ITEM_MAP.containsKey(item)) {
            for (int i = 0; i < NutritionMain.NUTRITION_ITEM_MAP.get(item).size(); i++) {
                if (NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i) > 0) {
                    addNutritionLevel(i, NutritionMain.NUTRITION_ITEM_MAP.get(item).get(i));
                }
            }
        }
    }

    // java/lang/Math.max (FF)F
    @Inject(method = "update", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(II)I", ordinal = 0))
    private void updateNutritionMixin(PlayerEntity player, CallbackInfo info) {
        decrementNutritionLevel(0, 1);
        decrementNutritionLevel(1, 1);
        decrementNutritionLevel(2, 1);
        decrementNutritionLevel(3, 1);
    }

    @Inject(method = "update", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/HungerManager;foodTickTimer:I", ordinal = 3))
    private void updateMixin(PlayerEntity player, CallbackInfo info) {
        if (this.foodTickTimer >= 60) { // or % 60 == 0
            // heal player when nutritions are good

            // player.heal(1.0f);
            // this.addExhaustion(6.0f);
            // this.foodTickTimer = 0;
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "update", at = @At("TAIL"))
    private void updateNutritionEffectsMixin(PlayerEntity player, CallbackInfo info) {
        if (player.getWorld().getTime() % 20 == 0) {
            List<Integer> list = List.of(this.carbohydrateLevel, this.proteinLevel, this.fatLevel, this.vitaminLevel);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) <= NutritionMain.NUTRITION_NEGATIVE_VALUE) {
                    List<Object> negativeEffectList = NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(i);
                    for (int u = 0; u < negativeEffectList.size(); u++) {
                        if (negativeEffectList.get(u) instanceof StatusEffectInstance) {
                            player.addStatusEffect((StatusEffectInstance) negativeEffectList.get(u));
                        } else if (!this.effectMap.get(i) && negativeEffectList.get(u) instanceof Multimap) {
                            player.getAttributes().addTemporaryModifiers((Multimap<EntityAttribute, EntityAttributeModifier>) negativeEffectList.get(u));
                        }
                    }
                    this.effectMap.put(i, true);
                } else if (list.get(i) >= NutritionMain.NUTRITION_POSITIVE_VALUE) {
                    List<Object> positiveEffectList = NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i);
                    for (int u = 0; u < positiveEffectList.size(); u++) {
                        if (positiveEffectList.get(u) instanceof StatusEffectInstance) {
                            player.addStatusEffect((StatusEffectInstance) positiveEffectList.get(u));
                        } else if (!this.effectMap.get(i) && positiveEffectList.get(u) instanceof Multimap) {
                            player.getAttributes().addTemporaryModifiers((Multimap<EntityAttribute, EntityAttributeModifier>) positiveEffectList.get(i));
                        }
                    }
                    this.effectMap.put(i, true);
                } else {
                    if (this.effectMap.get(i)) {
                        this.effectMap.put(i, false);
                        List<Object> positiveEffectList = NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i);
                        for (int u = 0; u < positiveEffectList.size(); u++) {
                            if (positiveEffectList.get(i) instanceof Multimap) {
                                player.getAttributes().removeModifiers((Multimap<EntityAttribute, EntityAttributeModifier>) positiveEffectList.get(u));
                            }
                        }
                        List<Object> negativeEffectList = NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(i);
                        for (int u = 0; u < negativeEffectList.size(); u++) {
                            if (negativeEffectList.get(i) instanceof Multimap) {
                                player.getAttributes().removeModifiers((Multimap<EntityAttribute, EntityAttributeModifier>) negativeEffectList.get(u));
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "addExhaustion", at = @At("TAIL"))
    private void addExhaustionMixin(float exhaustion, CallbackInfo info) {

    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readNbtMixin(NbtCompound nbt, CallbackInfo info) {
        this.carbohydrateLevel = nbt.getInt("CarbohydrateLevel");
        this.proteinLevel = nbt.getInt("ProteinLevel");
        this.fatLevel = nbt.getInt("FatLevel");
        this.vitaminLevel = nbt.getInt("VitaminLevel");
        // this.hasNegativeEffects = nbt.getBoolean("HasNegativeEffects");
        // this.hasPositiveEffects = nbt.getBoolean("HasPositiveEffects");
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeNbtMixin(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("CarbohydrateLevel", this.carbohydrateLevel);
        nbt.putInt("ProteinLevel", this.proteinLevel);
        nbt.putFloat("FatLevel", this.fatLevel);
        nbt.putFloat("VitaminLevel", this.vitaminLevel);
        // nbt.putBoolean("HasNegativeEffects", this.hasNegativeEffects);
        // nbt.putBoolean("HasPositiveEffects", this.hasPositiveEffects);
    }

    @Override
    public void addNutritionLevel(int type, int level) {
        if (type == 0) {
            this.carbohydrateLevel = (this.carbohydrateLevel + level > 40) ? 40 : (this.carbohydrateLevel + level);
        } else if (type == 1) {
            this.proteinLevel = (this.proteinLevel + level > 40) ? 40 : (this.proteinLevel + level);
        } else if (type == 2) {
            this.fatLevel = (this.fatLevel + level > 40) ? 40 : (this.fatLevel + level);
        } else if (type == 3) {
            this.vitaminLevel = (this.vitaminLevel + level > 40) ? 40 : (this.vitaminLevel + level);
        }
    }

    @Override
    public void decrementNutritionLevel(int type, int level) {
        if (type == 0) {
            this.carbohydrateLevel = (this.carbohydrateLevel - level < 0) ? 0 : (this.carbohydrateLevel - level);
        } else if (type == 1) {
            this.proteinLevel = (this.proteinLevel - level < 0) ? 0 : (this.proteinLevel - level);
        } else if (type == 2) {
            this.fatLevel = (this.fatLevel - level < 0) ? 0 : (this.fatLevel - level);
        } else if (type == 3) {
            this.vitaminLevel = (this.vitaminLevel - level < 0) ? 0 : (this.vitaminLevel - level);
        }
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
        }
    }

    @Override
    public int getNutritionLevel(int type) {
        switch (type) {
        case 0:
            return this.carbohydrateLevel;
        case 1:
            return this.proteinLevel;
        case 2:
            return this.fatLevel;
        case 3:
            return this.vitaminLevel;
        default:
            return 0;
        }
    }

    // When should the player not being able to sprint anymore? has to get synced to the client
    @Override
    public boolean canSprint() {
        // if(this.carbohydrateLevel){

        // }
        return true;
    }

}
