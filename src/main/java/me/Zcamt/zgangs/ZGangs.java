package me.Zcamt.zgangs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.Zcamt.zgangs.internals.Messages;
import me.Zcamt.zgangs.objects.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ZGangs extends JavaPlugin {

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
        if(this.getDataFolder().exists()){
            this.getDataFolder().mkdir();
        }

        if(messagesFile.exists()){
            saveResource("messages.yml", false);
        }

        Messages.reload();
    }

}
