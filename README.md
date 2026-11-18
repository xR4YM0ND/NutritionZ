<center>

![nutritionz title wide](https://cdn.modrinth.com/data/cached_images/e89b5dba6f292ca0b6118fb317af37b86f10d455.png)

NutritionZ adds a nutrition system to enhance the gameplay.

## Installation
NutritionZ is a mod built for both the [Fabric Loader](https://fabricmc.net/) and [NeoForge](https://neoforged.net/).

**Fabric:** requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) and [LibZ](https://www.curseforge.com/minecraft/mc-mods/libz) to be installed separately.  
**NeoForge:** requires [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) and [LibZ](https://www.curseforge.com/minecraft/mc-mods/libz) to be installed separately.

## Adjustments
### Default Datapacks & Mod Compat
NutritionZ provides some [default datapacks](https://github.com/xR4YM0ND/NutritionZ/tree/26.3/common/src/main/resources/resourcepacks) for other mods. 

</center>

<details>
<summary>26.3 default datapacks</summary>

- Farmers Delight
- Cultural Delights
- Easters Delight
- More Delight
- My Nethers Delight
- Rustic Delight
- Spanish Delight
- Ubes Delight
- Wilder Delights


- AdventureZ
- Aquaculture 2
- BetterEnd
- BetterNether
- Create
- Dehydration
- Ecologics
- Exline Fishing
- FarmZ
- Fishing Paradise
- Fish of Thieves
- Kaf Valentine Special
- Living Things
- More Berries
- Naturalist
- Nether & End
- Promenade
- Rose Gold Equipment
- Rustic Pancakes
- Starcatcher
- Tasty Vanilla
- Tofucraft
- Untamed
- UsefulFood Reborn
- Vanilla Rice
- WilderWild

</details>

<details>
<summary>1.21.1 default datapacks</summary>

- Farmers Delight
- More Delight
- Natures Delight
- Oceans Delight
- Ubes Delight


- Lets Do AlpineWhispers
- Lets Do Bakery
- Lets Do Beachparty
- Lets Do Brewery
- Lets Do Candlelight
- Lets Do Corn Expansion
- Lets Do Farm and Charm
- Lets Do HerbalBrews
- Lets Do Lilis Lucky Lures
- Lets Do Meadow
- Lets Do Vinery
- Lets Do WilderNature


- AdventureZ
- BetterEnd
- BetterNether
- Croptopia
- Dehydration
- FarmZ
- Living Things
- Natures Spirit
- Rose Gold Equipment
- The Bumblezone
- Tide 2
- Untamed
- WilderWild
- Yungs Cave Biomes

</details>

<details>
<summary>1.20.1 default datapacks</summary>

- Farmers Delight
- Cultural Delights
- Ends Delight
- Expanded Delight
- Farmers Delight
- Festive Delight
- More Delight
- Natures Delight
- Oceans Delight
- Ubes Delight


- Lets Do Bakery
- Lets Do Beachparty
- Lets Do Brewery
- Lets Do Candlelight
- Lets Do Corn Expansion
- Lets Do Farm and Charm
- Lets Do HerbalBrews
- Lets Do Lilis Lucky Lures
- Lets Do Meadow
- Lets Do Nether Vinery
- Lets Do Vinery
- Lets Do WilderNature


- AdAstra
- AdventureZ
- Amarite
- Aquamirae
- BetterEnd
- BetterNether
- BiomeMakeOver
- Bountiful Fares
- Brewin and Chewin
- Create
- Create Addition
- Culinaire
- Dehydration
- Exline Fishing
- Fish of Thieves
- Hybrid Aquatic
- Immersive Weathering
- Living Things
- Mob Catalog
- Naturalist
- Natures Spirit
- Nether Depths Upgrade
- Promenade
- Regions Unexplored
- Spawn
- Spelunkery
- Supplementaries
- The Bumblezone
- Tide
- Wilder Wild

</details>
<center>

You can disable/enable them via config.
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

## Links
[![GitHub License Badge](https://img.shields.io/github/license/xR4YM0ND/NutritionZ?style=for-the-badge&label=%C2%A7&labelColor=555555)](https://github.com/xR4YM0ND/NutritionZ/blob/26.3/LICENSE)
[![Source Code Badge](https://img.shields.io/badge/source_code-lightgray?style=for-the-badge&logo=github&label=%20&labelColor=555555)](https://github.com/xR4YM0ND/NutritionZ/tree/26.3)
[![Discord Badge](https://img.shields.io/discord/745451299713056791?style=for-the-badge&logo=discord&logoColor=white&label=%20&labelColor=555555&color=5865F2)](https://discord.gg/WXdSYn4yHB)

[![Modrinth Downloads Badge](https://img.shields.io/modrinth/dt/9IASI8W8?style=for-the-badge&logo=modrinth&logoColor=white&label=%20&labelColor=555555&color=1BD964)](https://modrinth.com/mod/nutritionz)
[![CurseForge Downloads Badge](https://img.shields.io/curseforge/dt/956695?style=for-the-badge&logo=curseforge&logoColor=white&label=%20&labelColor=555555&color=FF8041)](https://www.curseforge.com/minecraft/mc-mods/nutritionz)

</center>