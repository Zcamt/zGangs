package me.Zcamt.zgangs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.database.GangAdapter;
import me.Zcamt.zgangs.database.GangPlayerAdapter;
import me.Zcamt.zgangs.internals.Messages;
import me.Zcamt.zgangs.objects.*;
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

    @Override
    public void onEnable() {
        loadConfig();
    }

    public static Database getDatabase() {
        return database;
    }

    public void loadConfig(){
        File messagesFile = new File(this.getDataFolder(), "messages.yml");
        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdir();
        }

        if(!messagesFile.exists()){
            saveResource("messages.yml", false);
        }

        Messages.reload();
    }

}
