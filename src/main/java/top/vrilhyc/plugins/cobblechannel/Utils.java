package top.vrilhyc.plugins.cobblechannel;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Utils {
    public static void sendMessage(ServerPlayerEntity serverPlayer,String content){
        serverPlayer.sendChatMessage(new SentMessage.Chat(SignedMessage.ofUnsigned(serverPlayer.getUuid(),content)), false, MessageType.params(MessageType.CHAT, serverPlayer));
    }

    public static void displayMessage(ServerPlayerEntity serverPlayer,String content){
        serverPlayer.sendMessageToClient(Text.literal(content),false);
    }
}
