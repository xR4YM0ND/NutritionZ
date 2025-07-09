<center>

![nutritionz title wide](https://cdn.modrinth.com/data/cached_images/e89b5dba6f292ca0b6118fb317af37b86f10d455.png)

NutritionZ adds a nutrition system to enhance the gameplay.

## Installation
NutritionZ is a mod built for the [Fabric Loader](https://fabricmc.net/). It requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) to be installed separately; all other dependencies are installed with the mod.

## Adjustments
### Default Datapacks & Mod Compat
NutritionZ provides some [default datapacks](https://github.com/xR4YM0ND/NutritionZ/tree/1.21/src/main/resources/resourcepacks) for other mods. You can disable/enable them via config.
You can add other mod compats via datapack. Just follow the structure `data/somemod/nutrition/somename.json`.

Here's an example from the default vanilla datapack:

</center>

```json
{
    "minecraft:cooked_cod": { // The default datapacks provide 5 points for half saturation
        "carbohydrates": 0,
        "protein": 16,
        "fat": 3,
        "vitamins": 0,
        "minerals": 6
    }
}
```

<center>

### Nutrition Manager
You can tweak the [default manager](https://github.com/xR4YM0ND/NutritionZ/blob/1.21/src/main/resources/data/nutritionz/nutrition_manager/default.json) via datapack as well. `data/somemod/nutrition_manager/somename.json`

With this you can replace the positive effects & attributes from all 5 nutrients, as well as the negatives.

Example:
</center>

```json
{
    "minerals": {
        "replace": true,
        "positive": {
            "generic.armor_toughness": { // attribute
                "operation": "ADD_VALUE", // ADD_VALUE, ADD_MULTIPLIED_BASE , ADD_MULTIPLIED_TOTAL
                "value": 1.0 // gives +1 Armor Toughness per level -> e.g. +20 max level
            },
            "minecraft:haste": { // effect
                "duration": 319, // wouldn`t recommend to lower this on effects
                "amplifier": 0 // equals Haste I
            }
        },
        "negative": {
            "generic.armor_toughness": {
                "operation": "ADD_VALUE",
                "value": 1.0 // needs to be positive value -> will be negative automatically
            },
            "minecraft:mining_fatigue": {
                "duration": 319,
                "amplifier": 0
            }
        }
    }
}
```

<center>

Max Nutrition & thresholds can be set in the `nutritionz.json5` config.

</center>

```json
{
	"maxNutrition": 100, // Max nutrition for one nutrient
	"negativeNutrition": 10, // 0 - 10 = negative effects / attributes (negative threshold)
	"positiveNutrition": 90, // 90 - 100 = positive effects / attributes (positive threshold)
}
```

<center>

## Credits
<a href="https://discord.gg/WXdSYn4yHB" target="_blank">
<img alt="Discord Fabricated Atelier" src="https://img.shields.io/discord/745451299713056791?color=7289DA&label=DISCORD&logo=discord&logoColor=white&style=for-the-badge"></a>
<a href="https://github.com/xR4YM0ND/NutritionZ/blob/1.21/LICENSE" target="_blank">
<img alt="GitHub License" src="https://img.shields.io/github/license/xR4YM0ND/NutritionZ?style=for-the-badge"></a>
<a href="https://github.com/xR4YM0ND/NutritionZ" target="_blank">
<img alt="GitHub Source Code" src="https://img.shields.io/badge/Github-Source_Code-lightgrey?style=for-the-badge"></a>
<p></p>
<a href="https://modrinth.com/user/xR4YM0ND" target="_blank">
<img alt="xR4YM0ND Modrinth" src="https://img.shields.io/badge/Modrinth-xR4YM0ND-1bd96a?style=for-the-badge"></a>
<a href="https://legacy.curseforge.com/members/spigotde/projects" target="_blank"><img alt="xR4YM0ND Curseforge" src="https://img.shields.io/badge/Curseforge-xR4YM0ND-f16436?style=for-the-badge"></a>
<p></p>
</center>