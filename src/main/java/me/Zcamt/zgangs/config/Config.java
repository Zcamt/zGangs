package me.Zcamt.zgangs.config;

import com.google.common.collect.ImmutableList;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class Config {

    private static final File configFile = new File(Bukkit.getPluginManager().getPlugin("zGangs").getDataFolder(), "config.yml");
    public static YamlConfiguration config;

    public static String prefix, gangchatPrefix, allychatPrefix, nameRegex;

    public static int maxNameLength, minNameLength;
    public static int transferGangCost, createGangCost, gangRenameCost;

    public static ImmutableList<String> bannedNames;

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

        bannedNames = ImmutableList.copyOf(config.getStringList("gangs.banned-names"));
    }

}
