package me.Zcamt.zgangs.config;

import com.google.common.collect.ImmutableList;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config {

    private static final File configFile = new File(Bukkit.getPluginManager().getPlugin("zGangs").getDataFolder(), "config.yml");
    public static YamlConfiguration config;

    public static String prefix, nameRegex;

    public static int defaultMaxMembers, defaultMaxAllies, maxNameLength, minNameLength;
    public static int transferGangCost, createGangCost, rankUpCost;

    public static ImmutableList<String> bannedNames;

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);

        prefix = ChatUtil.CC(config.getString("prefix"));
    }

}
