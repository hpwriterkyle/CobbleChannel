package top.vrilhyc.plugins.cobblechannel;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface Channel {
    Map<UUID,String> channels = new HashMap<>();
    Map<String,Channel> registeredChannels = new HashMap<>();
    StaffChannel STAFF_CHANNEL = registerChannel(new StaffChannel());
    AdminChannel ADMIN_CHANNEL = registerChannel(new AdminChannel());
    BuilderChannel BUILDER_CHANNEL = registerChannel(new BuilderChannel());
    String getChannelName();
    default boolean sendMessage(ServerPlayerEntity serverPlayer,SignedMessage message){
        if(!isInChannel(serverPlayer)){
            return false;
        }
        serverPlayer.getServer().getPlayerManager().getPlayerList().stream().filter(a->isInChannel(a)).forEach(a->Utils.sendMessage(a,message.getSignedContent()));
        return true;
    }
    default boolean isInChannel(ServerPlayerEntity serverPlayer){
        return channels.containsKey(serverPlayer.getUuid());
    }
    default boolean joinChannel(ServerPlayerEntity serverPlayer){
        if(isInChannel(serverPlayer)){
            return false;
        }
        channels.put(serverPlayer.getUuid(),getChannelName());
        return true;
    }
    default boolean leaveChannel(ServerPlayerEntity serverPlayer){
        if(!isInChannel(serverPlayer)){
            return false;
        }
        channels.remove(serverPlayer.getUuid());
        return true;
    }

    static <C extends Channel>C registerChannel(C channel){
        registeredChannels.put(channel.getChannelName(),channel);
        return channel;
    }
}
