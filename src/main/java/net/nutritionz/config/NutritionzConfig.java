package net.nutritionz.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.loader.api.FabricLoader;
import net.nutritionz.config.NutritionzConfig.LetsDoCategory;
import net.nutritionz.config.NutritionzConfig.MISCCategory;

@Config(name = "nutritionz")
// @Config.Gui.Background("minecraft:textures/block/stone.png")
public class NutritionzConfig implements ConfigData {

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
  public int posX = 162;
  @ConfigEntry.Category("general")
  public int posY = 5;

  public static class Icons {
    public String carbohydrateItemId = "minecraft:sugar";
    public String proteinItemId = "minecraft:chicken";
    public String fatItemId = "minecraft:porkchop";
    public String vitaminItemId = "minecraft:apple";
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
    public boolean fdMoredelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdNaturesdelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdOceansdelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fdUbesdelightDefaultCompat = true;
  }

  @ConfigEntry.Category("compat")
  @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
  public LetsDoCategory letsdo = new LetsDoCategory();

  public static class LetsDoCategory {
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoBakeryDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoBeachpartyDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoBreweryDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoCandlelightDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoCornexpansionDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoFarmcharmDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoHerbalbrewsDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoLllDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoMeadowDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoVineryDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean letsdoWildernatureDefaultCompat = true;
  }

  @ConfigEntry.Category("compat")
  @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
  public MISCCategory misc = new MISCCategory();

  public static class MISCCategory {
    @ConfigEntry.Gui.RequiresRestart
    public boolean adventurezDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean betterendDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean betternetherDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean croptopiaDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean dehydrationDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean dehydrationmeadowDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean dehydrationvineryDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean farmzDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean fishofthievesDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean livingthingsDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean naturesspiritDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean rosegoldequipmentDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean scorchfulDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean thebumblezoneDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean tideDefaultCompat = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean wilderwildDefaultCompat = true;
  }

}
