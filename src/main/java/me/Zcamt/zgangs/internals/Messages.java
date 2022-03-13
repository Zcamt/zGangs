package me.Zcamt.zgangs.internals;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages {

    private static final File messagesFile = new File(Bukkit.getPluginManager().getPlugin("zGangs").getDataFolder(), "message.yml");
    public static YamlConfiguration config;

    private static String invalidUsage;
    private static String noPerm;
    private static String notInGang;
    private static String invalidPlayer;
    private static String invalidGang;
    private static String illegalGangName;

    public static String invalidUsage(String usage) {
        return invalidUsage.replace("{usage}", usage);
    }

    public static String noPerm() {
        return noPerm;
    }

    public static String notInGang() {
        return notInGang;
    }

    public static String invalidPlayer() {
        return invalidPlayer;
    }

    public static String invalidGang() {
        return invalidGang;
    }

    public static String illegalGangName() {
        return illegalGangName;
    }


    public static void reload() {
        config = YamlConfiguration.loadConfiguration(messagesFile);

        invalidUsage = ChatUtil.CC(config.getString("invalid-usage"));
        noPerm = ChatUtil.CC(config.getString("no-perm"));
        notInGang = ChatUtil.CC(config.getString("not-in-gang"));
        invalidPlayer = ChatUtil.CC(config.getString("invalid-player"));
        invalidGang = ChatUtil.CC(config.getString("invalid-gang"));
        illegalGangName = ChatUtil.CC(config.getString("illegal-gangname"));
    }

}
