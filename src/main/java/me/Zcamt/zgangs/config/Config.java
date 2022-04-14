package me.Zcamt.zgangs.config;

import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    private static final File configFile = new File(Bukkit.getPluginManager().getPlugin("zGangs").getDataFolder(), "config.yml");
    public static YamlConfiguration config;

    private static String prefix;

    public static int defaultMaxMembers, defaultMaxAllies;

    public static String prefix() {
        return prefix;
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);

        prefix = ChatUtil.CC(config.getString("prefix"));
    }

}
