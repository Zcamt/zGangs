package me.Zcamt.zgangs;

import me.Zcamt.zgangs.api.ZGangsAPI;
import me.Zcamt.zgangs.listeners.PlayerJoinListener;
import me.Zcamt.zgangs.managers.CommandManager;
import me.Zcamt.zgangs.managers.DatabaseManager;
import me.Zcamt.zgangs.managers.GangManager;
import me.Zcamt.zgangs.managers.GangPlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class ZGangs extends JavaPlugin {

    private final DatabaseManager databaseManager = new DatabaseManager(this);
    private final GangManager gangManager = new GangManager(databaseManager);
    private final GangPlayerManager gangPlayerManager = new GangPlayerManager(databaseManager);
    private final CommandManager commandManager = new CommandManager(gangManager, gangPlayerManager);
    private final ZGangsAPI api = new ZGangsAPI();

    //Todo: Remember to close all connections to DB.

    @Override
    public void onEnable() {
        getLogger().info("zGangs");
        getLogger().info("Made by Zcamt");
        try {
            databaseManager.setup();
            System.out.println("Connected to SQL Database!");
        } catch (SQLException e) {
            getServer().getPluginManager().disablePlugin(this);
            getLogger().severe("Shutting down!");
            throw new RuntimeException("Couldn't connect to SQL Database!" +
                    "\nPlease check config.yml and check the SQL information you've provided", e);
        }

        commandManager.registerCommands(this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(gangPlayerManager, gangManager), this);

    }

    //Todo: Make onDisable

    public ZGangsAPI getApi() {
        return api;
    }
}
