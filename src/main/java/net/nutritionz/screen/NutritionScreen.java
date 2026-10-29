package net.nutritionz.screen;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.nutritionz.init.KeyInit;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.init.ConfigInit;
import net.nutritionz.init.RenderInit;

@Environment(EnvType.CLIENT)
public class NutritionScreen extends Screen {

    private int x;
    private int y;
    private final List<ItemStack> nutritionItems = List.of(new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.icons.carbohydrateItemId))),
            new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.icons.proteinItemId))), new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.icons.fatItemId))),
            new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.icons.vitaminItemId))), new ItemStack(Registries.ITEM.get(Identifier.of(ConfigInit.CONFIG.icons.mineralItemId))));
    private final List<Text> nutritionTexts = List.of(Text.translatable("screen.nutritionz.carbohydrates"), Text.translatable("screen.nutritionz.protein"), Text.translatable("screen.nutritionz.fat"),
            Text.translatable("screen.nutritionz.vitamins"), Text.translatable("screen.nutritionz.minerals"));
    @Nullable
    private HungerManagerAccess hungerManagerAccess = null;

    public NutritionScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.x = this.width / 2 - (176 / 2);
        this.y = this.height / 2 - (141 / 2);
        this.hungerManagerAccess = this.client != null && this.client.player != null ? (HungerManagerAccess) this.client.player.getHungerManager() : null;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawTexture(RenderInit.NUTRITION_ICONS, this.x, this.y, 0, 0, 176, 142);
        context.drawText(this.textRenderer, this.title, this.x + 176 / 2 - this.textRenderer.getWidth(this.title) / 2, this.y + 7, 0x3F3F3F, false);
        int extraY = 0;
        int extraBarY = 0;
        for (int i = 0; i < this.nutritionItems.size(); i++) {
            context.drawItem(this.nutritionItems.get(i), this.x + 7, this.y + 25 + extraY);
            context.drawText(this.textRenderer, this.nutritionTexts.get(i), this.x + 28, this.y + 26 + extraY, 0x3F3F3F, false);
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 206 + extraBarY, 141, 5);
            if (this.hungerManagerAccess != null) {
                int level = this.hungerManagerAccess.getNutritionLevel(i);
                int maxNutrition = ConfigInit.CONFIG.maxNutrition;
                int negativeWidth = 140 * ConfigInit.CONFIG.negativeNutrition / maxNutrition;
                int positiveX = 140 * ConfigInit.CONFIG.positiveNutrition / maxNutrition;
                if (level > 0) {
                    context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 211 + extraBarY, 140 * level / maxNutrition, 5);
                }
                context.fill(this.x + 27 + negativeWidth, this.y + 36 + extraY, this.x + 28 + negativeWidth, this.y + 41 + extraY, 0x803F3F3F);
                context.fill(this.x + 27 + positiveX, this.y + 36 + extraY, this.x + 28 + positiveX, this.y + 41 + extraY, 0x803F3F3F);
                context.drawText(this.textRenderer, Text.translatable("screen.nutritionz.nutritionValue", level, maxNutrition), this.x + 127, this.y + 26 + extraY, 0x3F3F3F, false);
                List<Text> tooltips = new ArrayList<>();
                if (isPointWithinBounds(27, 36 + extraY, negativeWidth, 5, mouseX, mouseY)) {
                    addEffectTooltips(tooltips, NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(i),
                            Text.translatable("screen.nutritionz.negativeEffects", ConfigInit.CONFIG.negativeNutrition).formatted(Formatting.RED), level <= ConfigInit.CONFIG.negativeNutrition);
                } else if (isPointWithinBounds(27 + positiveX, 36 + extraY, 141 - positiveX, 5, mouseX, mouseY)) {
                    addEffectTooltips(tooltips, NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i),
                            Text.translatable("screen.nutritionz.positiveEffects", ConfigInit.CONFIG.positiveNutrition, maxNutrition).formatted(Formatting.GREEN),
                            level > ConfigInit.CONFIG.negativeNutrition && level >= ConfigInit.CONFIG.positiveNutrition);
                }
                if (!tooltips.isEmpty()) {
                    context.drawTooltip(textRenderer, tooltips, mouseX, mouseY);
                }
            }
            extraY += 23;
            extraBarY += 10;
        }
        if (isPointWithinBounds(5, 5, 11, 10, mouseX, mouseY)) {
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 5, this.y + 5, 187, 0, 11, 10);
        } else {
            context.drawTexture(RenderInit.NUTRITION_ICONS, this.x + 5, this.y + 5, 176, 0, 11, 10);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isPointWithinBounds(5, 5, 11, 10, mouseX, mouseY)) {
            this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.client.setScreen(new InventoryScreen(this.client.player));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        }
        if (KeyInit.screenKey.matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @SuppressWarnings("unchecked")
    private void addEffectTooltips(List<Text> tooltips, @Nullable List<Object> effects, MutableText header, boolean active) {
        tooltips.add(active ? header.append(Text.translatable("screen.nutritionz.active")) : header);
        if (effects == null || effects.isEmpty()) {
            tooltips.add(Text.translatable("screen.nutritionz.noEffects").formatted(Formatting.GRAY));
            return;
        }
        for (Object effect : effects) {
            if (effect instanceof StatusEffectInstance statusEffectInstance) {
                StatusEffect statusEffect = statusEffectInstance.getEffectType().value();
                Text name = statusEffect.getName();
                if (statusEffectInstance.getAmplifier() > 0) {
                    name = Text.translatable("potion.withAmplifier", name, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
                }
                tooltips.add(name.copy().formatted(statusEffect.getCategory().getFormatting()));
            } else if (effect instanceof Multimap<?, ?> multimap) {
                ((Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier>) multimap).forEach((attribute, modifier) -> {
                    double value = modifier.value();
                    double displayValue;
                    if (modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                        displayValue = value * 100.0;
                    } else if (attribute.matches(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
                        displayValue = value * 10.0;
                    } else {
                        displayValue = value;
                    }
                    Text attributeName = Text.translatable(attribute.value().getTranslationKey());
                    if (value > 0.0) {
                        tooltips.add(Text.translatable("attribute.modifier.plus." + modifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(displayValue), attributeName)
                                .formatted(attribute.value().getFormatting(true)));
                    } else if (value < 0.0) {
                        tooltips.add(Text.translatable("attribute.modifier.take." + modifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(-displayValue), attributeName)
                                .formatted(attribute.value().getFormatting(false)));
                    }
                });
            }
        }
    }

    private boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        int i = this.x;
        int j = this.y;
        return (pointX -= (double) i) >= (double) (x - 1) && pointX < (double) (x + width + 1) && (pointY -= (double) j) >= (double) (y - 1) && pointY < (double) (y + height + 1);
    }

}
