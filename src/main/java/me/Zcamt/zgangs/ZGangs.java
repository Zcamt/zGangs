package me.Zcamt.zgangs;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.Zcamt.zgangs.commands.GangAdminCommand;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.database.GangAdapter;
import me.Zcamt.zgangs.database.GangPlayerAdapter;
import me.Zcamt.zgangs.managers.GangLevelManager;
import me.Zcamt.zgangs.managers.GangManager;
import me.Zcamt.zgangs.managers.GangPlayerManager;
import me.Zcamt.zgangs.managers.LeaderboardManager;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ZGangs extends JavaPlugin {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Gang.class, new GangAdapter())
            .registerTypeAdapter(GangPlayer.class, new GangPlayerAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    private static final Database DATABASE = new Database();
    private static final GangManager GANG_MANAGER = new GangManager(DATABASE);
    private static final GangPlayerManager GANG_PLAYER_MANAGER = new GangPlayerManager(DATABASE);
    private static final LeaderboardManager LEADERBOARD_MANAGER = new LeaderboardManager();
    private static final GangLevelManager GANG_LEVEL_MANAGER = new GangLevelManager();

    @Override
    public void onEnable() {
        loadConfig();
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new GangAdminCommand());
    }



    private void loadConfig(){
        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdir();
        }

        saveResource("config.yml", false);
        saveResource("messages.yml", false);

        Config.reload();
        Messages.reload();
    }

    public static Database getDatabase() {
        return DATABASE;
    }

    public static GangManager getGangManager() {
        return GANG_MANAGER;
    }

    public static GangPlayerManager getGangPlayerManager() {
        return GANG_PLAYER_MANAGER;
    }

    public static LeaderboardManager getLeaderboardManager() {
        return LEADERBOARD_MANAGER;
    }

    public static GangLevelManager getGangLevelManager() {
        return GANG_LEVEL_MANAGER;
    }
}
