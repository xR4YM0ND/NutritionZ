package net.nutritionz.mixin;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.Before;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.init.ConfigInit;

@Mixin(HungerManager.class)
public class HungerManagerMixin implements HungerManagerAccess {

    private int carbohydrateLevel = ConfigInit.CONFIG.maxNutrition;
    private int proteinLevel = ConfigInit.CONFIG.maxNutrition;
    private int fatLevel = ConfigInit.CONFIG.maxNutrition;
    private int vitaminLevel = ConfigInit.CONFIG.maxNutrition;
    private int mineralLevel = ConfigInit.CONFIG.maxNutrition;
    private Map<Integer, Boolean> effectMap = new HashMap<Integer, Boolean>() {
        {
            put(0, false);
            put(1, false);
            put(2, false);
            put(3, false);
            put(4, false);
        }
    };
    // Unused
    private boolean shouldUpdateNutritions = false;

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

    @Inject(method = "update", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(II)I", ordinal = 0))
    private void updateNutritionMixin(PlayerEntity player, CallbackInfo info) {
        decrementNutritionLevel(0, 1);
        decrementNutritionLevel(1, 1);
        decrementNutritionLevel(2, 1);
        decrementNutritionLevel(3, 1);
        decrementNutritionLevel(4, 1);
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void updateMixin(PlayerEntity player, CallbackInfo info) {
        if (this.foodTickTimer % 5 == 0 && this.shouldUpdateNutritions) {
            this.shouldUpdateNutritions = false;
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "update", at = @At("TAIL"))
    private void updateNutritionEffectsMixin(PlayerEntity player, CallbackInfo info) {
        if (!player.isCreative() && player.getWorld().getTime() % 20 == 0) {

            // System.out.println(NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(2));
            // player.getAttributes().mofi

            boolean changedAttributes = false;
            List<Integer> list = List.of(this.carbohydrateLevel, this.proteinLevel, this.fatLevel, this.vitaminLevel, this.mineralLevel);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) <= ConfigInit.CONFIG.negativeNutrition) {
                    List<Object> negativeEffectList = NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(i);
                    if (negativeEffectList != null && !negativeEffectList.isEmpty()) {
                        for (int u = 0; u < negativeEffectList.size(); u++) {
                            if (negativeEffectList.get(u) instanceof StatusEffectInstance statusEffectInstance) {
                                if (!player.hasStatusEffect(statusEffectInstance.getEffectType())
                                        || player.getStatusEffect(statusEffectInstance.getEffectType()).getDuration() < statusEffectInstance.getDuration() - 50) {
                                    player.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
                                }
                            } else if (!this.effectMap.get(i) && negativeEffectList.get(u) instanceof Multimap) {
                                player.getAttributes().addTemporaryModifiers((Multimap<EntityAttribute, EntityAttributeModifier>) negativeEffectList.get(u));
                                changedAttributes = true;
                            }
                        }
                        this.effectMap.put(i, true);
                    }
                } else if (list.get(i) >= ConfigInit.CONFIG.positiveNutrition) {
                    List<Object> positiveEffectList = NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i);
                    if (positiveEffectList != null && !positiveEffectList.isEmpty()) {
                        for (int u = 0; u < positiveEffectList.size(); u++) {
                            if (positiveEffectList.get(u) instanceof StatusEffectInstance statusEffectInstance) {
                                if (!player.hasStatusEffect(statusEffectInstance.getEffectType())
                                        || player.getStatusEffect(statusEffectInstance.getEffectType()).getDuration() < statusEffectInstance.getDuration() - 50) {
                                    player.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
                                }
                            } else if (!this.effectMap.get(i) && positiveEffectList.get(u) instanceof Multimap map) {

                                System.out.println("Before Added attributes: " + player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).getModifiers() + " : "
                                        + ((Multimap<EntityAttribute, EntityAttributeModifier>) positiveEffectList.get(u)));
                                // player.getAttributes().addTemporaryModifiers((Multimap<EntityAttribute, EntityAttributeModifier>) positiveEffectList.get(u));
                                player.getAttributes().addTemporaryModifiers(ArrayListMultimap.create(map));
                                // Multimaps.unmodifiableMultimap(map);

                                System.out.println("Added attributes: " + player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).getModifiers()+ " : "+ArrayListMultimap.create(map));

                                // player.getAttributes().removeModifiers((Multimap<EntityAttribute, EntityAttributeModifier>) positiveEffectList.get(u));
                                // System.out.println("After removed attributes: " + player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).getModifiers());
                                changedAttributes = true;
                            }

                            // [Server thread/INFO] (Minecraft) [STDOUT]: Before Added attributes: [] : {net.minecraft.entity.attribute.ClampedEntityAttribute@553fbe94=[AttributeModifier{amount=2.0,
                            // operation=ADDITION, name='attribute.name.generic.armor', id=d5ed1a75-69e5-47f5-95c4-0e8fd02e149e}]}
                            // [19:02:24] [Server thread/INFO] (Minecraft) [STDOUT]: Added attributes: [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor',
                            // id=d5ed1a75-69e5-47f5-95c4-0e8fd02e149e}]
                        }
                        this.effectMap.put(i, true);
                    }
                } else {
                    if (this.effectMap.get(i)) {
                        this.effectMap.put(i, false);
                        List<Object> positiveEffectList = NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i);
                        for (int u = 0; u < positiveEffectList.size(); u++) {
                            if (positiveEffectList.get(u) instanceof Multimap multimap) {
                                // System.out.println("BEFORE: " + player.getAttributes().getValue(EntityAttributes.GENERIC_ARMOR));
                                // ((Multimap<EntityAttribute, EntityAttributeModifier>) positiveEffectList.get(u)).forEach((eattribute, eamodifier) -> {
                                // if (player.getAttributes().getCustomInstance(eattribute) != null) {

                                // player.getAttributes().getCustomInstance(eattribute).removeModifier(eamodifier);
                                // System.out.println(eamodifier.getName() + " : " + player.getAttributes().getCustomInstance(eattribute).tryRemoveModifier(eamodifier.getId()) + " : "
                                // + player.getAttributes().getCustomInstance(eattribute).getModifiers() + " : "
                                // + player.getAttributes().getCustomInstance(eattribute).getModifier(eamodifier.getId()) + " : " + eamodifier.getId());
                                // }
                                // });

                                // [19:09:04] [Server thread/INFO] (Minecraft) [STDOUT]: Before Added attributes: [] :
                                // {net.minecraft.entity.attribute.ClampedEntityAttribute@553fbe94=[AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor',
                                // id=4b844a6a-572a-473c-b217-5c08ab61f91b}]}

                                // [19:09:04] [Server thread/INFO] (Minecraft) [STDOUT]: Added attributes: [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor',
                                // id=4b844a6a-572a-473c-b217-5c08ab61f91b}]

                                // [19:09:34] [Server thread/INFO] (Minecraft) [STDOUT]: BEFORE REMOVE: [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor',
                                // id=4b844a6a-572a-473c-b217-5c08ab61f91b}] : {net.minecraft.entity.attribute.ClampedEntityAttribute@553fbe94=[AttributeModifier{amount=2.0, operation=ADDITION,
                                // name='attribute.name.generic.armor', id=71605017-10ef-4c20-80fd-00580501f496}]}

                                // [19:09:34] [Server thread/INFO] (Minecraft) [STDOUT]: AFTER REMOVE: [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor',
                                // id=4b844a6a-572a-473c-b217-5c08ab61f91b}]

                                System.out.println("BEFORE REMOVE: " + player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ARMOR).getModifiers() + "   :   "
                                        + (Multimap<EntityAttribute, EntityAttributeModifier>) positiveEffectList.get(u) + " : " + NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i).get(u) + " : "
                                        + multimap);
                                player.getAttributes().removeModifiers((Multimap<EntityAttribute, EntityAttributeModifier>) positiveEffectList.get(u));
                                System.out.println("AFTER REMOVE: " + player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ARMOR).getModifiers());

                                changedAttributes = true;
                                // System.out.println("AFTER: " + player.getAttributes().getValue(EntityAttributes.GENERIC_ARMOR));
                                // System.out.println("REMOVE" + ((Multimap<EntityAttribute, EntityAttributeModifier>) positiveEffectList.get(u)).values());
                            } // 30dc89b2-ee07-4319-a9d1-02123c1abf82

                            // Added attributes: [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor', id=d5ed1a75-69e5-47f5-95c4-0e8fd02e149e}]

                            // Before Added attributes: [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor', id=d5ed1a75-69e5-47f5-95c4-0e8fd02e149e}] :
                            // {net.minecraft.entity.attribute.ClampedEntityAttribute@1d585fb=[AttributeModifier{amount=0.10000000149011612, operation=ADDITION,
                            // name='attribute.name.generic.knockback_resistance', id=aeaf4641-0a52-4523-acfe-82758363e6b3}]}
                            // [19:00:52] [Server thread/INFO] (Minecraft) [STDOUT]: Added attributes: [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor',
                            // id=d5ed1a75-69e5-47f5-95c4-0e8fd02e149e}]
                        }
                        List<Object> negativeEffectList = NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(i);
                        for (int u = 0; u < negativeEffectList.size(); u++) {
                            if (negativeEffectList.get(u) instanceof Multimap) {
                                player.getAttributes().removeModifiers((Multimap<EntityAttribute, EntityAttributeModifier>) negativeEffectList.get(u));
                                changedAttributes = true;
                            }
                        }
                    }
                }
            }
            if (changedAttributes) {
                Collection<EntityAttributeInstance> collection = player.getAttributes().getAttributesToSend();
                if (!collection.isEmpty()) {
                    // collection.stream().forEach(test -> {
                    // test.getModifiers().forEach(lol -> {
                    // System.out.println(lol.getName() + " : " + lol.getValue());
                    // });
                    // });
                    // System.out.println("Test: " + player.getAttributes().getValue(EntityAttributes.GENERIC_ARMOR));
                    ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityAttributesS2CPacket(player.getId(), collection));
                }
            }
        }
    }

    // [18:55:26] [Server thread/INFO] (Minecraft) [STDOUT]: Before Added attributes: [] : {net.minecraft.entity.attribute.ClampedEntityAttribute@553fbe94=[AttributeModifier{amount=2.0,
    // operation=ADDITION, name='attribute.name.generic.armor', id=5b57c0c8-c908-46ee-86d0-113b4157f86a}]}
    // [18:55:26] [Server thread/INFO] (Minecraft) [STDOUT]: Added attributes: [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor',
    // id=5b57c0c8-c908-46ee-86d0-113b4157f86a}]

    // [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor', id=5b57c0c8-c908-46ee-86d0-113b4157f86a}] :
    // {net.minecraft.entity.attribute.ClampedEntityAttribute@553fbe94=[AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor',
    // id=d5ed1a75-69e5-47f5-95c4-0e8fd02e149e}]}
    // [18:56:44] [Server thread/INFO] (Minecraft) [STDOUT]: AFTER REMOVE: [AttributeModifier{amount=2.0, operation=ADDITION, name='attribute.name.generic.armor',
    // id=5b57c0c8-c908-46ee-86d0-113b4157f86a}]

    @Inject(method = "addExhaustion", at = @At("TAIL"))
    private void addExhaustionMixin(float exhaustion, CallbackInfo info) {
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readNbtMixin(NbtCompound nbt, CallbackInfo info) {
        this.carbohydrateLevel = nbt.getInt("CarbohydrateLevel");
        this.proteinLevel = nbt.getInt("ProteinLevel");
        this.fatLevel = nbt.getInt("FatLevel");
        this.vitaminLevel = nbt.getInt("VitaminLevel");
        this.mineralLevel = nbt.getInt("MineralLevel");
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeNbtMixin(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("CarbohydrateLevel", this.carbohydrateLevel);
        nbt.putInt("ProteinLevel", this.proteinLevel);
        nbt.putFloat("FatLevel", this.fatLevel);
        nbt.putFloat("VitaminLevel", this.vitaminLevel);
        nbt.putFloat("MineralLevel", this.mineralLevel);
    }

    @Override
    public void addNutritionLevel(int type, int level) {
        if (type == 0) {
            this.carbohydrateLevel = (this.carbohydrateLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.carbohydrateLevel + level);
        } else if (type == 1) {
            this.proteinLevel = (this.proteinLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.proteinLevel + level);
        } else if (type == 2) {
            this.fatLevel = (this.fatLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.fatLevel + level);
        } else if (type == 3) {
            this.vitaminLevel = (this.vitaminLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.vitaminLevel + level);
        } else if (type == 4) {
            this.mineralLevel = (this.mineralLevel + level > ConfigInit.CONFIG.maxNutrition) ? ConfigInit.CONFIG.maxNutrition : (this.mineralLevel + level);
        }
        this.shouldUpdateNutritions = true;
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
        } else if (type == 4) {
            this.mineralLevel = (this.mineralLevel - level < 0) ? 0 : (this.mineralLevel - level);
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
        switch (type) {
        case 0:
            return this.carbohydrateLevel;
        case 1:
            return this.proteinLevel;
        case 2:
            return this.fatLevel;
        case 3:
            return this.vitaminLevel;
        case 4:
            return this.mineralLevel;
        default:
            return 0;
        }
    }

}
