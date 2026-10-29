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
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.nutritionz.api.NutritionApi;
import net.nutritionz.api.NutritionType;

public class CommandInit {

    private static final SuggestionProvider<ServerCommandSource> NUTRITION_SUGGESTION_PROVIDER = (context, builder) -> CommandSource.suggestMatching(
            Arrays.stream(NutritionType.values()).map(NutritionType::getId), builder);
    private static final DynamicCommandExceptionType INVALID_TYPE_EXCEPTION = new DynamicCommandExceptionType(type -> Text.translatable("commands.nutritionz.invalidType", type));

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("nutrition").requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.literal("get").then(CommandManager.argument("target", EntityArgumentType.player())
                            .then(CommandManager.argument("type", StringArgumentType.word()).suggests(NUTRITION_SUGGESTION_PROVIDER).executes(context -> {
                                return executeGet(context.getSource(), EntityArgumentType.getPlayer(context, "target"), getType(context));
                            }))))
                    .then(CommandManager.literal("set").then(CommandManager.argument("targets", EntityArgumentType.players())
                            .then(CommandManager.argument("type", StringArgumentType.word()).suggests(NUTRITION_SUGGESTION_PROVIDER)
                                    .then(CommandManager.argument("value", IntegerArgumentType.integer(0)).executes(context -> {
                                        return executeChange(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), getType(context),
                                                IntegerArgumentType.getInteger(context, "value"), false);
                                    })))))
                    .then(CommandManager.literal("add").then(CommandManager.argument("targets", EntityArgumentType.players())
                            .then(CommandManager.argument("type", StringArgumentType.word()).suggests(NUTRITION_SUGGESTION_PROVIDER)
                                    .then(CommandManager.argument("value", IntegerArgumentType.integer()).executes(context -> {
                                        return executeChange(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), getType(context),
                                                IntegerArgumentType.getInteger(context, "value"), true);
                                    }))))));
        });
    }

    private static NutritionType getType(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String typeId = StringArgumentType.getString(context, "type");
        NutritionType type = NutritionType.byId(typeId);
        if (type == null) {
            throw INVALID_TYPE_EXCEPTION.create(typeId);
        }
        return type;
    }

    private static int executeGet(ServerCommandSource source, ServerPlayerEntity serverPlayerEntity, NutritionType type) {
        int level = NutritionApi.getNutrition(serverPlayerEntity, type);
        source.sendFeedback(() -> Text.translatable("commands.nutritionz.get", serverPlayerEntity.getDisplayName(), level, NutritionApi.getMaxNutrition(),
                Text.translatable(type.getTranslationKey())), false);
        return level;
    }

    private static int executeChange(ServerCommandSource source, Collection<ServerPlayerEntity> targets, NutritionType type, int value, boolean add) {
        for (ServerPlayerEntity serverPlayerEntity : targets) {
            if (add) {
                NutritionApi.addNutrition(serverPlayerEntity, type, value);
            } else {
                NutritionApi.setNutrition(serverPlayerEntity, type, value);
            }
        }
        String key = "commands.nutritionz." + (add ? "add" : "set") + (targets.size() == 1 ? ".single" : ".multiple");
        Text typeText = Text.translatable(type.getTranslationKey());
        if (targets.size() == 1) {
            ServerPlayerEntity serverPlayerEntity = targets.iterator().next();
            int level = NutritionApi.getNutrition(serverPlayerEntity, type);
            source.sendFeedback(() -> Text.translatable(key, value, typeText, serverPlayerEntity.getDisplayName(), level), true);
        } else {
            source.sendFeedback(() -> Text.translatable(key, value, typeText, targets.size()), true);
        }
        return targets.size();
    }
}
