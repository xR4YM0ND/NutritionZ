# NutritionZ
NutritionZ adds a nutrition system to enhance the gameplay.

## Installation
NutritionZ is a mod built for the [Fabric Loader](https://fabricmc.net/). It requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) to be installed separately; all other dependencies are installed with the mod.

## Adjustments
### Default Datapacks & Mod Compat
NutritionZ provides some [default datapacks](https://github.com/xR4YM0ND/NutritionZ/tree/1.21/src/main/resources/resourcepacks) for other mods. You can disable/enable them via config.
You can add other mod compats via datapack. Just follow the structure `data/somemod/nutrition/somename.json`.

Here's an example from the default vanilla datapack:
```json
{
    "minecraft:cooked_cod": {
        "carbohydrates": 0,
        "protein": 16,
        "fat": 3,
        "vitamins": 0,
        "minerals": 6
    }
}
```

### Nutrition Manager
You can tweak the [default manager](https://github.com/xR4YM0ND/NutritionZ/blob/1.21/src/main/resources/data/nutritionz/nutrition_manager/default.json) via datapack as well. `data/somemod/nutrition_manager/somename.json`

With this you can replace the positive effects & attributes from all 5 nutrients, as well as the negatives.

Example:
```json
{
    "carbohydrates": {
        "replace": true,
        "positive": {
            "generic.attack_speed": {
                "operation": "ADD_VALUE",
                "value": 0.3
            },
            "generic.movement_speed": {
                "operation": "ADD_VALUE",
                "value": 0.01
            }
        },
        "negative": {
            "generic.attack_speed": {
                "operation": "ADD_VALUE",
                "value": 0.3
            },
            "generic.movement_speed": {
                "operation": "ADD_VALUE",
                "value": 0.01
            }
        }
    }
}
```

## License
NutritionZ is licensed under MIT.