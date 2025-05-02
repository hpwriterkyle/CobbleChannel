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
    default String getChannelDisplayName(){
        return getChannelName();
    }
    default String getFormat(){
        return "[%channel] %player | %content";
    }
    default String formatted(ServerPlayerEntity serverPlayer,SignedMessage message){
        return getFormat().replace("%channel",getChannelDisplayName()).replace("%player",serverPlayer.getName().getLiteralString()).replace("%content",message.getSignedContent());
    }
    default boolean sendMessage(ServerPlayerEntity serverPlayer,SignedMessage message){
        if(!isInChannel(serverPlayer)){
            return false;
        }
        serverPlayer.getServer().getPlayerManager().getPlayerList().stream().filter(this::isInChannel).forEach(a->Utils.displayMessage(a,formatted(serverPlayer,message)));
        return true;
    }
    default boolean isInChannel(ServerPlayerEntity serverPlayer){
        return channels.getOrDefault(serverPlayer.getUuid(),"").equalsIgnoreCase(getChannelName());
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
