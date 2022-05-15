package me.Zcamt.zgangs;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.Zcamt.zgangs.commands.GangAdminCommand;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.database.*;
import me.Zcamt.zgangs.objects.gang.GangAdapter;
import me.Zcamt.zgangs.objects.gang.gangallies.GangAllies;
import me.Zcamt.zgangs.objects.gang.gangallies.GangAlliesAdapter;
import me.Zcamt.zgangs.objects.gang.gangitem.GangItemDelivery;
import me.Zcamt.zgangs.objects.gang.gangitem.GangItemDeliveryAdapter;
import me.Zcamt.zgangs.objects.gang.ganglevel.GangLevelManager;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.gangmembers.GangMembers;
import me.Zcamt.zgangs.objects.gang.gangmembers.GangMembersAdapter;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermissions;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermissionsAdapter;
import me.Zcamt.zgangs.objects.gang.gangrivals.GangRivals;
import me.Zcamt.zgangs.objects.gang.gangrivals.GangRivalsAdapter;
import me.Zcamt.zgangs.objects.gang.gangstats.GangStats;
import me.Zcamt.zgangs.objects.gang.gangstats.GangStatsAdapter;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerAdapter;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.objects.leaderboard.LeaderboardManager;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class ZGangs extends JavaPlugin {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Gang.class, new GangAdapter())
            .registerTypeAdapter(GangPlayer.class, new GangPlayerAdapter())
            .registerTypeAdapter(GangAllies.class, new GangAlliesAdapter())
            .registerTypeAdapter(GangRivals.class, new GangRivalsAdapter())
            .registerTypeAdapter(GangMembers.class, new GangMembersAdapter())
            .registerTypeAdapter(GangStats.class, new GangStatsAdapter())
            .registerTypeAdapter(GangPermissions.class, new GangPermissionsAdapter())
            .registerTypeAdapter(GangItemDelivery.class, new GangItemDeliveryAdapter())
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
