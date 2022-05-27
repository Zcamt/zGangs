package me.Zcamt.zgangs.config;

import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages {

    private static final File messagesFile = new File(Bukkit.getPluginManager().getPlugin("zGangs").getDataFolder(), "messages.yml");
    public static YamlConfiguration config;

    private static String invalidUsage;
    private static String neededGangRank = "&cFejl: Du skal være {rank} i din bande for at gøre dette";
    public static String noPerm;
    public static String notInGang;
    public static String cantWhileInGang = "&cFejl: Det kan du ikke gøre når du er medlem af en bande";
    public static String invalidPlayer;
    public static String invalidGang;
    public static String illegalGangName;
    public static String unknownCommand;
    public static String invalidInput;
    public static String noEnoughMoney = "&cFejl: Det har du ikke penge nok til";
    public static String unexpectedError = "&cFejl: Noget gik galt, kontakt venligst en Admin";

    public static String invalidUsage(String usage) {
        return invalidUsage.replace("{usage}", usage);
    }

    public static String neededGangRank(String neededRank) {
        return neededGangRank.replace("{rank}", neededRank);
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(messagesFile);

        invalidUsage = ChatUtil.CC(config.getString("invalid-usage"));
        noPerm = ChatUtil.CC(config.getString("no-perm"));
        notInGang = ChatUtil.CC(config.getString("not-in-gang"));
        invalidPlayer = ChatUtil.CC(config.getString("invalid-player"));
        invalidGang = ChatUtil.CC(config.getString("invalid-gang"));
        illegalGangName = ChatUtil.CC(config.getString("illegal-gangname"));
        unknownCommand = ChatUtil.CC(config.getString("unknown-command"));
        invalidInput = ChatUtil.CC(config.getString("invalid-input"));
    }

}
