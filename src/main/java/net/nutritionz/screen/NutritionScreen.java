package net.nutritionz.screen;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;
import net.nutritionz.init.KeyInit;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.nutritionz.NutritionMain;
import net.nutritionz.access.HungerManagerAccess;
import net.nutritionz.init.ConfigInit;
import net.nutritionz.init.RenderInit;

@Environment(EnvType.CLIENT)
public class NutritionScreen extends Screen {

    private int x;
    private int y;
    private final List<ItemStack> nutritionItems = List.of(new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(ConfigInit.CONFIG.icons.carbohydrateItemId))),
            new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(ConfigInit.CONFIG.icons.proteinItemId))), new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(ConfigInit.CONFIG.icons.fatItemId))),
            new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(ConfigInit.CONFIG.icons.vitaminItemId))), new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(ConfigInit.CONFIG.icons.mineralItemId))));
    private final List<Component> nutritionTexts = List.of(Component.translatable("screen.nutritionz.carbohydrates"), Component.translatable("screen.nutritionz.protein"), Component.translatable("screen.nutritionz.fat"),
            Component.translatable("screen.nutritionz.vitamins"), Component.translatable("screen.nutritionz.minerals"));
    @Nullable
    private HungerManagerAccess hungerManagerAccess = null;

    public NutritionScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.x = this.width / 2 - (176 / 2);
        this.y = this.height / 2 - (141 / 2);
        this.hungerManagerAccess = this.minecraft != null && this.minecraft.player != null ? (HungerManagerAccess) this.minecraft.player.getFoodData() : null;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);

        context.blit(RenderPipelines.GUI_TEXTURED, RenderInit.NUTRITION_ICONS, this.x, this.y, 0, 0, 176, 142, 256, 256);
        context.text(this.font, this.title, this.x + 176 / 2 - this.font.width(this.title) / 2, this.y + 7, 0xFF3F3F3F, false);
        int extraY = 0;
        int extraBarY = 0;
        for (int i = 0; i < this.nutritionItems.size(); i++) {
            context.item(this.nutritionItems.get(i), this.x + 7, this.y + 25 + extraY);
            context.text(this.font, this.nutritionTexts.get(i), this.x + 28, this.y + 26 + extraY, 0xFF3F3F3F, false);
            context.blit(RenderPipelines.GUI_TEXTURED, RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 206 + extraBarY, 141, 5, 256, 256);
            if (this.hungerManagerAccess != null) {
                int level = this.hungerManagerAccess.getNutritionLevel(i);
                int maxNutrition = ConfigInit.CONFIG.maxNutrition;
                int negativeWidth = 140 * ConfigInit.CONFIG.negativeNutrition / maxNutrition;
                int positiveX = 140 * ConfigInit.CONFIG.positiveNutrition / maxNutrition;
                if (level > 0) {
                    context.blit(RenderPipelines.GUI_TEXTURED, RenderInit.NUTRITION_ICONS, this.x + 27, this.y + 36 + extraY, 0, 211 + extraBarY, 140 * level / maxNutrition, 5, 256, 256);
                }
                context.fill(this.x + 27 + negativeWidth, this.y + 36 + extraY, this.x + 28 + negativeWidth, this.y + 41 + extraY, 0x803F3F3F);
                context.fill(this.x + 27 + positiveX, this.y + 36 + extraY, this.x + 28 + positiveX, this.y + 41 + extraY, 0x803F3F3F);
                context.text(this.font, Component.translatable("screen.nutritionz.nutritionValue", level, maxNutrition), this.x + 127, this.y + 26 + extraY, 0xFF3F3F3F, false);
                List<Component> tooltips = new ArrayList<>();
                if (isPointWithinBounds(27, 36 + extraY, negativeWidth, 5, mouseX, mouseY)) {
                    addEffectTooltips(tooltips, NutritionMain.NUTRITION_NEGATIVE_EFFECTS.get(i),
                            Component.translatable("screen.nutritionz.negativeEffects", ConfigInit.CONFIG.negativeNutrition).withStyle(ChatFormatting.RED), level <= ConfigInit.CONFIG.negativeNutrition);
                } else if (isPointWithinBounds(27 + positiveX, 36 + extraY, 141 - positiveX, 5, mouseX, mouseY)) {
                    addEffectTooltips(tooltips, NutritionMain.NUTRITION_POSITIVE_EFFECTS.get(i),
                            Component.translatable("screen.nutritionz.positiveEffects", ConfigInit.CONFIG.positiveNutrition, maxNutrition).withStyle(ChatFormatting.GREEN),
                            level > ConfigInit.CONFIG.negativeNutrition && level >= ConfigInit.CONFIG.positiveNutrition);
                }
                if (!tooltips.isEmpty()) {
                    context.setComponentTooltipForNextFrame(this.font, tooltips, mouseX, mouseY);
                }
            }
            extraY += 23;
            extraBarY += 10;
        }
        if (isPointWithinBounds(5, 5, 11, 10, mouseX, mouseY)) {
            context.blit(RenderPipelines.GUI_TEXTURED, RenderInit.NUTRITION_ICONS, this.x + 5, this.y + 5, 187, 0, 11, 10, 256, 256);
        } else {
            context.blit(RenderPipelines.GUI_TEXTURED, RenderInit.NUTRITION_ICONS, this.x + 5, this.y + 5, 176, 0, 11, 10, 256, 256);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (isPointWithinBounds(5, 5, 11, 10, event.x(), event.y())) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.minecraft.gui.setScreen(new InventoryScreen(this.minecraft.player));
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.minecraft.options.keyInventory.matches(event)) {
            this.onClose();
            return true;
        }
        if (KeyInit.screenKey.matches(event)) {
            this.onClose();
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @SuppressWarnings("unchecked")
    private void addEffectTooltips(List<Component> tooltips, @Nullable List<Object> effects, MutableComponent header, boolean active) {
        tooltips.add(active ? header.append(Component.translatable("screen.nutritionz.active")) : header);
        if (effects == null || effects.isEmpty()) {
            tooltips.add(Component.translatable("screen.nutritionz.noEffects").withStyle(ChatFormatting.GRAY));
            return;
        }
        for (Object effect : effects) {
            if (effect instanceof MobEffectInstance statusEffectInstance) {
                MobEffect statusEffect = statusEffectInstance.getEffect().value();
                Component name = statusEffect.getDisplayName();
                if (statusEffectInstance.getAmplifier() > 0) {
                    name = Component.translatable("potion.withAmplifier", name, Component.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
                }
                tooltips.add(name.copy().withStyle(statusEffect.getCategory().getTooltipFormatting()));
            } else if (effect instanceof Multimap<?, ?> multimap) {
                ((Multimap<Holder<Attribute>, AttributeModifier>) multimap).forEach((attribute, modifier) -> {
                    double value = modifier.amount();
                    double displayValue;
                    if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                        displayValue = value * 100.0;
                    } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
                        displayValue = value * 10.0;
                    } else {
                        displayValue = value;
                    }
                    Component attributeName = Component.translatable(attribute.value().getDescriptionId());
                    if (value > 0.0) {
                        tooltips.add(Component.translatable("attribute.modifier.plus." + modifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayValue), attributeName)
                                .withStyle(attribute.value().getStyle(true)));
                    } else if (value < 0.0) {
                        tooltips.add(Component.translatable("attribute.modifier.take." + modifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-displayValue), attributeName)
                                .withStyle(attribute.value().getStyle(false)));
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
