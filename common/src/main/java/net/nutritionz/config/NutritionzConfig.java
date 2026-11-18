package net.nutritionz.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.libz.api.ConfigSync;
import net.nutritionz.init.ConfigInit;

@Config(name = "nutritionz")
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class NutritionzConfig implements ConfigData, ConfigSync {

  @ConfigEntry.Category("general")
  @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
  public Icons icons = new Icons();

  @ConfigEntry.Category("general")
  public int maxNutrition = 100;
  @ConfigEntry.Category("general")
  public int negativeNutrition = 10;
  @ConfigEntry.Category("general")
  public int positiveNutrition = 90;
  @ConfigEntry.Category("general")
  @ConfigSync.ClientOnly
  public int posX = 162;
  @ConfigEntry.Category("general")
  @ConfigSync.ClientOnly
  public int posY = 5;

  public static class Icons {
    @ConfigSync.ClientOnly
    public String carbohydrateItemId = "minecraft:sugar";
    @ConfigSync.ClientOnly
    public String proteinItemId = "minecraft:chicken";
    @ConfigSync.ClientOnly
    public String fatItemId = "minecraft:porkchop";
    @ConfigSync.ClientOnly
    public String vitaminItemId = "minecraft:apple";
    @ConfigSync.ClientOnly
    public String mineralItemId = "minecraft:iron_nugget";
  }

  @ConfigEntry.Category("compat")
  @ConfigEntry.Gui.RequiresRestart
  public boolean minecraftDefaultCompat = true;

  @ConfigEntry.Category("compat")
  @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
  public FarmersDelightCategory fd = new FarmersDelightCategory();

  public static class FarmersDelightCategory {
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdFarmersdelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdCulturaldelightsDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdEastersdelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdMoredelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdMynethersdelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdRusticdelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdSpanishdelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdUbesdelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdWilderdelightsDefaultCompat = true;
  }

  @ConfigEntry.Category("compat")
  @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
  public MISCCategory misc = new MISCCategory();

  public static class MISCCategory {
    @ConfigEntry.Gui.RequiresRestart
    public boolean adventurezDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean aquacultureDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean betterendDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean betternetherDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean createDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean dehydrationDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean ecologicsDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean exlinefishingDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean farmzDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fishingparadiseDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fishofthievesDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean kafvalentineDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean livingthingsDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean moreberriesDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean naturalistDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean netherandendDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean promenadeDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean rosegoldequipmentDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean rusticpancakesDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean starcatcherDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean tastyvanillaDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean tofucraftDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean untamedDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean usefulfoodDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean vanillariceDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean wilderwildDefaultCompat = true;
  }


    @Override
    public void validatePostLoad() {
        this.maxNutrition = Math.max(1, this.maxNutrition);
        this.negativeNutrition = Math.clamp(this.negativeNutrition, 0, this.maxNutrition);
        this.positiveNutrition = Math.clamp(this.positiveNutrition, this.negativeNutrition, this.maxNutrition);
    }

    @Override
    public void updateConfig(ConfigData data) {
        ConfigInit.CONFIG = (NutritionzConfig) data;
    }
}
