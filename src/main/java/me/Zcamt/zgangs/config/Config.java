package me.Zcamt.zgangs.config;

import com.google.common.collect.ImmutableList;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    private static final File configFile = new File(Bukkit.getPluginManager().getPlugin("zGangs").getDataFolder(), "config.yml");
    public static YamlConfiguration config;

    public static String prefix, gangchatPrefix, allychatPrefix, nameRegex;

    public static int maxNameLength, minNameLength;
    public static int transferGangCost, createGangCost, gangRenameCost;

    public static int cigPrice;
    public static int gangAreaCPrice, gangAreaBPrice, gangAreaAPrice;
    public static int gangDamageUpgradeStartPrice, gangDamageUpgradeIncreaseCoefficient;
    public static int allyDamageUpgradeStartPrice, allyDamageUpgradeIncreaseCoefficient;
    public static int memberUpgradeStartPrice, memberUpgradeIncreaseCoefficient;
    public static int allyUpgradeStartPrice, allyUpgradeIncreaseCoefficient;

    public static ImmutableList<String> bannedWords;

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);

        prefix = ChatUtil.CC(config.getString("settings.prefix"));
        gangchatPrefix = ChatUtil.CC(config.getString("settings.gangchat-prefix"));
        allychatPrefix = ChatUtil.CC(config.getString("settings.allychat-prefix"));
        nameRegex = config.getString("gangs.name-regex");

        maxNameLength = config.getInt("gangs.max-name-length");
        minNameLength = config.getInt("gangs.min-name-length");
        transferGangCost = config.getInt("gangs.gang-transfer-cost");
        createGangCost = config.getInt("gangs.gang-create-cost");
        gangRenameCost = config.getInt("gangs.gang-rename-cost");

        cigPrice = config.getInt("gangs.shop.cig-price");

        gangAreaCPrice = config.getInt("gangs.shop.gang-area-c-price");
        gangAreaBPrice = config.getInt("gangs.shop.gang-area-b-price");
        gangAreaAPrice = config.getInt("gangs.shop.gang-area-a-price");

        gangDamageUpgradeStartPrice = config.getInt("gangs.shop.gang-damage.start-price");
        gangDamageUpgradeIncreaseCoefficient = config.getInt("gangs.shop.gang-damage.pr-upgrade-increase-coefficient");

        allyDamageUpgradeStartPrice = config.getInt("gangs.shop.ally-damage.start-price");
        allyDamageUpgradeIncreaseCoefficient = config.getInt("gangs.shop.ally-damage.pr-upgrade-increase-coefficient");

        memberUpgradeStartPrice = config.getInt("gangs.shop.member-count.start-price");
        memberUpgradeIncreaseCoefficient = config.getInt("gangs.shop.member-count.pr-upgrade-increase-coefficient");

        allyUpgradeStartPrice = config.getInt("gangs.shop.ally-count.start-price");
        allyUpgradeIncreaseCoefficient = config.getInt("gangs.shop.ally-count.pr-upgrade-increase-coefficient");

        bannedWords = ImmutableList.copyOf(config.getStringList("gangs.banned-words"));
    }

}
