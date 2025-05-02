package top.vrilhyc.plugins.cobblechannel.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.minecraft.command.CommandSource;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import top.vrilhyc.plugins.cobblechannel.Channel;
import top.vrilhyc.plugins.cobblechannel.Utils;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static top.vrilhyc.plugins.cobblechannel.RequiredArgumentBuilder.argument;

public class ChannelCommand {
    private static final String MESSAGE_KEY = "message";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = literal("channel");
        LiteralArgumentBuilder<ServerCommandSource> scBuilder = literal("sc");
        scBuilder.executes(a->{
            String channelName = "staff";
            if (!(a.getSource().getPlayer() instanceof ServerPlayerEntity serverPlayer)) {
                return 1;
            }
            return channel(serverPlayer,channelName);
        });
        LiteralArgumentBuilder<ServerCommandSource> bcBuilder = literal("bc");
        bcBuilder.executes(a->{
            String channelName = "builder";
            if (!(a.getSource().getPlayer() instanceof ServerPlayerEntity serverPlayer)) {
                return 1;
            }
            return channel(serverPlayer,channelName);
        });
        LiteralArgumentBuilder<ServerCommandSource> acBuilder = literal("ac");
        acBuilder.executes(a->{
            String channelName = "admin";
            if (!(a.getSource().getPlayer() instanceof ServerPlayerEntity serverPlayer)) {
                return 1;
            }
            return channel(serverPlayer,channelName);
        });

        builder.then(argument("channelName", StringArgumentType.string()).suggests((commandContext, suggestionsBuilder) -> {
            Channel.registeredChannels.keySet().forEach(suggestionsBuilder::suggest);
            return suggestionsBuilder.buildFuture();
        }).executes(a->{
                    String channelName = StringArgumentType.getString(a,"channelName");
                    if (!(a.getSource().getPlayer() instanceof ServerPlayerEntity serverPlayer)) {
                        return 1;
                    }
                    return channel(serverPlayer,channelName);
                })
        );

        dispatcher.register(builder);
        dispatcher.register(scBuilder);
        dispatcher.register(bcBuilder);
        dispatcher.register(acBuilder);
    }

    public static boolean isSomeGroup(UUID who,String group) {
//        try {
//            return LuckPermsProvider.get().getUserManager().loadUser(who)
//                    .thenApplyAsync(user -> {
//                        Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
//                        return inheritedGroups.stream().anyMatch(g -> g.getName().equals(group));
//                    }).get()
//
//
//                    ;
//        } catch (Exception ignored) {
//        return true;
        try {
            return Permissions.check(who, "cobblechannel." + group).get();
        }catch (Exception|Error ex){
            return true;
        }
//        }
    }

    public static int channel(ServerPlayerEntity serverPlayer,String channelName){
        Channel channel = Channel.registeredChannels.get(channelName);
        if(channel==null){
            Utils.displayMessage(serverPlayer,"There is no channel %s".formatted(channelName));
            return 1;
        }
        if(isSomeGroup(serverPlayer.getUuid(),channelName)){
            if(channel.joinChannel(serverPlayer)) {
                Utils.displayMessage(serverPlayer,"You have successfully joined the %s channel".formatted(channelName));
                return 1;
            }
            channel.leaveChannel(serverPlayer);
            Utils.displayMessage(serverPlayer,"You have successfully leaved the %s channel".formatted(channelName));
        }
        return 1;
    }
}
