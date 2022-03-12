package me.Zcamt.zgangs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.Zcamt.zgangs.objects.*;
import org.bukkit.plugin.java.JavaPlugin;

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

    }

    public static Database getDatabase() {
        return database;
    }
}
