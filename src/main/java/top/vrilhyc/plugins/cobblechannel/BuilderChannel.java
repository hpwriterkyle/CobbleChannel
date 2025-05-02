package top.vrilhyc.plugins.cobblechannel;

public class BuilderChannel implements Channel{
    @Override
    public String getChannelName() {
        return "builder";
    }

    @Override
    public String getChannelDisplayName() {
        return "BC";
    }

    @Override
    public String getFormat() {
        return "§6[%channel]§r %player | §6%content";
    }
}
