package me.Zcamt.zgangs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.database.GangAdapter;
import me.Zcamt.zgangs.database.GangPlayerAdapter;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.managers.GangManager;
import me.Zcamt.zgangs.managers.GangPlayerManager;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ZGangs extends JavaPlugin {

    //Todo: Add "java docs" to gang and gang player methods perhaps.

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Gang.class, new GangAdapter())
            .registerTypeAdapter(GangPlayer.class, new GangPlayerAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    private static final Database database = new Database();
    private static final GangManager GANG_MANAGER = new GangManager(database);
    private static final GangPlayerManager GANG_PLAYER_MANAGER = new GangPlayerManager(database);

    @Override
    public void onEnable() {
        loadConfig();
    }

    public void loadConfig(){
        File configFile = new File(this.getDataFolder(), "config.yml");
        File messagesFile = new File(this.getDataFolder(), "messages.yml");
        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdir();
        }

        if(!configFile.exists()){
            saveResource("config.yml", false);
        }
        if(!messagesFile.exists()){
            saveResource("messages.yml", false);
        }

        Messages.reload();
    }

    public static Database getDatabase() {
        return database;
    }

    public static GangManager getGangManager() {
        return GANG_MANAGER;
    }

    public static GangPlayerManager getGangPlayerManager() {
        return GANG_PLAYER_MANAGER;
    }

}
