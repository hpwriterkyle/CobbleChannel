package top.vrilhyc.plugins.cobblechannel;

public class AdminChannel implements Channel{
    @Override
    public String getChannelName() {
        return "admin";
    }

    @Override
    public String getChannelDisplayName() {
        return "AC";
    }

    @Override
    public String getFormat() {
        return "§c[%channel]§r %player | §c%content";
    }
}
