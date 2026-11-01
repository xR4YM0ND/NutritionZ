package net.nutritionz.init;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.nutritionz.api.NutritionApi;
import net.nutritionz.api.NutritionType;

public class CommandInit {

    private static final SuggestionProvider<CommandSourceStack> NUTRITION_SUGGESTION_PROVIDER = (context, builder) -> SharedSuggestionProvider.suggest(
            Arrays.stream(NutritionType.values()).map(NutritionType::getId), builder);
    private static final DynamicCommandExceptionType INVALID_TYPE_EXCEPTION = new DynamicCommandExceptionType(type -> Component.translatable("commands.nutritionz.invalidType", type));

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("nutrition").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .then(Commands.literal("get").then(Commands.argument("target", EntityArgument.player())
                            .then(Commands.argument("type", StringArgumentType.word()).suggests(NUTRITION_SUGGESTION_PROVIDER).executes(context -> {
                                return executeGet(context.getSource(), EntityArgument.getPlayer(context, "target"), getType(context));
                            }))))
                    .then(Commands.literal("set").then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("type", StringArgumentType.word()).suggests(NUTRITION_SUGGESTION_PROVIDER)
                                    .then(Commands.argument("value", IntegerArgumentType.integer(0)).executes(context -> {
                                        return executeChange(context.getSource(), EntityArgument.getPlayers(context, "targets"), getType(context),
                                                IntegerArgumentType.getInteger(context, "value"), false);
                                    })))))
                    .then(Commands.literal("add").then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("type", StringArgumentType.word()).suggests(NUTRITION_SUGGESTION_PROVIDER)
                                    .then(Commands.argument("value", IntegerArgumentType.integer()).executes(context -> {
                                        return executeChange(context.getSource(), EntityArgument.getPlayers(context, "targets"), getType(context),
                                                IntegerArgumentType.getInteger(context, "value"), true);
                                    }))))));
        });
    }

    private static NutritionType getType(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String typeId = StringArgumentType.getString(context, "type");
        NutritionType type = NutritionType.byId(typeId);
        if (type == null) {
            throw INVALID_TYPE_EXCEPTION.create(typeId);
        }
        return type;
    }

    private static int executeGet(CommandSourceStack source, ServerPlayer serverPlayerEntity, NutritionType type) {
        int level = NutritionApi.getNutrition(serverPlayerEntity, type);
        source.sendSuccess(() -> Component.translatable("commands.nutritionz.get", serverPlayerEntity.getDisplayName(), level, NutritionApi.getMaxNutrition(),
                Component.translatable(type.getTranslationKey())), false);
        return level;
    }

    private static int executeChange(CommandSourceStack source, Collection<ServerPlayer> targets, NutritionType type, int value, boolean add) {
        for (ServerPlayer serverPlayerEntity : targets) {
            if (add) {
                NutritionApi.addNutrition(serverPlayerEntity, type, value);
            } else {
                NutritionApi.setNutrition(serverPlayerEntity, type, value);
            }
        }
        String key = "commands.nutritionz." + (add ? "add" : "set") + (targets.size() == 1 ? ".single" : ".multiple");
        Component typeText = Component.translatable(type.getTranslationKey());
        if (targets.size() == 1) {
            ServerPlayer serverPlayerEntity = targets.iterator().next();
            int level = NutritionApi.getNutrition(serverPlayerEntity, type);
            source.sendSuccess(() -> Component.translatable(key, value, typeText, serverPlayerEntity.getDisplayName(), level), true);
        } else {
            source.sendSuccess(() -> Component.translatable(key, value, typeText, targets.size()), true);
        }
        return targets.size();
    }
}
