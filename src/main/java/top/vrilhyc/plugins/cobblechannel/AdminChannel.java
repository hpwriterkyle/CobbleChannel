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
}
