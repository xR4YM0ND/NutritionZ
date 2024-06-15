# NutritionZ
NutritionZ adds a nutrition system to enhance the gameplay.

## Installation
NutritionZ is a mod built for the [Fabric Loader](https://fabricmc.net/). It requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) to be installed separately; all other dependencies are installed with the mod.

## License
NutritionZ is licensed under MIT.

## Datapacks
You can add custom mod support with datapacks.
Just create a [datapack](https://minecraft.wiki/w/Data_pack) with a `pack.mcmeta` & a version for your minecraft version.

Then you just need a folder structure for the mod e.g. `data/modid/nutrition/sometext.json`.

In this json you can add the mods items & give them nutrient values.  

```json
{
    "modid:itemid": {
        "carbohydrates": 0,
        "protein": 0,
        "fat": 0,
        "vitamins": 0,
        "minerals": 0
    },
    "modid:itemid2": {
        "carbohydrates": 0,
        "protein": 0,
        "fat": 0,
        "vitamins": 0,
        "minerals": 0
    }
}
```
### More Infos
*The default datapack gives 5 values per half hunger.*  
You also currently can't overwrite the default datapacks (items & values added via them)
