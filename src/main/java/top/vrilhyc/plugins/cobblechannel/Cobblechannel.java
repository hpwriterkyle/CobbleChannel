package top.vrilhyc.plugins.cobblechannel;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import top.vrilhyc.plugins.cobblechannel.commands.ChannelCommand;

public class Cobblechannel implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((signedMessage, serverPlayerEntity, parameters) -> {
            SignedMessage message = SignedMessage.ofUnsigned(serverPlayerEntity.getUuid(),signedMessage.getSignedContent());
            return !Channel.STAFF_CHANNEL.sendMessage(serverPlayerEntity,message);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> {
            ChannelCommand.register(dispatcher);
        });
    }
}
