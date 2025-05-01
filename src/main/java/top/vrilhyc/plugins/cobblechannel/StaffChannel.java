package top.vrilhyc.plugins.cobblechannel;

import net.minecraft.server.network.ServerPlayerEntity;

public class StaffChannel implements Channel{
    @Override
    public String getChannelName() {
        return "staff";
    }
}
