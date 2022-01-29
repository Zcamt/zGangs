package me.Zcamt.zgangs;

import me.Zcamt.zgangs.listeners.PlayerJoinListener;
import me.Zcamt.zgangs.managers.GangPlayerManager;
import me.Zcamt.zgangs.api.ZGangsAPI;
import me.Zcamt.zgangs.listeners.MenuListener;
import me.Zcamt.zgangs.managers.CommandManager;
import me.Zcamt.zgangs.managers.Database;
import me.Zcamt.zgangs.managers.GangManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ZGangs extends JavaPlugin {

    private final Database database = new Database();
    private final GangManager gangManager = new GangManager(database);
    private final GangPlayerManager gangPlayerManager = new GangPlayerManager(database);
    private final CommandManager commandManager = new CommandManager(gangManager, gangPlayerManager);
    private final ZGangsAPI api = new ZGangsAPI();

    //Todo: Try and come up with a solution to the possibility of misalligned information gang -> player and vise versa.
    // Fx. if a player is not set to be in a gang but the gang has them under members?

    //Todo: Make sure empty invite-lists doesn't get messed up in serialization and de-serialization either when saved and retrieved from the DB

    //Todo: Need to change GangPlayer to use UUID instead of Player variable

    @Override
    public void onEnable() {
        getLogger().info("zGangs");
        getLogger().info("Made by Zcamt");

        database.setup();

        commandManager.registerCommands(this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(gangPlayerManager, gangManager), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

    }

    @Override
    public void onDisable() {
        //Todo: Make sure to save current cache to DB
        database.disconnect();
    }

    public ZGangsAPI getApi() {
        return api;
    }
}
