package me.Zcamt.zgangs;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.Zcamt.zgangs.chatinput.ChatInputManager;
import me.Zcamt.zgangs.commands.GangAdminCommand;
import me.Zcamt.zgangs.commands.MainCommand;
import me.Zcamt.zgangs.commands.MenuCommand;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.listeners.ChatInputListener;
import me.Zcamt.zgangs.listeners.InventoryListener;
import me.Zcamt.zgangs.listeners.PlayerListener;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangAdapter;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.gangallies.GangAllies;
import me.Zcamt.zgangs.objects.gang.gangallies.GangAlliesAdapter;
import me.Zcamt.zgangs.objects.gang.gangitem.GangItemDelivery;
import me.Zcamt.zgangs.objects.gang.gangitem.GangItemDeliveryAdapter;
import me.Zcamt.zgangs.objects.gang.ganglevel.GangLevelManager;
import me.Zcamt.zgangs.objects.gang.gangmembers.GangMembers;
import me.Zcamt.zgangs.objects.gang.gangmembers.GangMembersAdapter;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermissions;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermissionsAdapter;
import me.Zcamt.zgangs.objects.gang.gangrivals.GangRivals;
import me.Zcamt.zgangs.objects.gang.gangrivals.GangRivalsAdapter;
import me.Zcamt.zgangs.objects.gang.gangstats.GangStats;
import me.Zcamt.zgangs.objects.gang.gangstats.GangStatsAdapter;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerAdapter;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.objects.leaderboard.LeaderboardManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.PermissionUtil;
import me.Zcamt.zgangs.utils.Permissions;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
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
    private static final ChatInputManager CHAT_INPUT_MANAGER = new ChatInputManager();

    @Override
    public void onEnable() {
        loadConfig();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        super.onDisable();
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

    private void registerCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new GangAdminCommand());
        commandManager.registerCommand(new MainCommand());
        commandManager.registerCommand(new MenuCommand());

        commandManager.getCommandConditions().addCondition("no-gang", context -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            Player player = issuer.getPlayer();
            GangPlayer gangPlayer = getGangPlayerManager().findById(player.getUniqueId());
            if(gangPlayer.isInGang()){
                ChatUtil.sendMessage(player, Messages.cantWhileInGang);
                throw new ConditionFailedException();
            }
        });

        commandManager.getCommandConditions().addCondition("in-gang", context -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            Player player = issuer.getPlayer();
            GangPlayer gangPlayer = getGangPlayerManager().findById(player.getUniqueId());
            if(!gangPlayer.isInGang()){
                ChatUtil.sendMessage(player, Messages.notInGang);
                throw new ConditionFailedException();
            }
        });

        commandManager.getCommandConditions().addCondition("is-player", context -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            if(!issuer.isPlayer()) {
                issuer.sendMessage("Kun spillere kan bruge den kommando");
                throw new ConditionFailedException();
            }
            Player player = issuer.getPlayer();
            if (!PermissionUtil.hasPermissionWithMessage(player, Permissions.PLAYER.getPermission(), null)) {
                throw new ConditionFailedException();
            }
        });

        commandManager.getCommandConditions().addCondition("is-admin", context -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            if(!issuer.isPlayer()) {
                issuer.sendMessage("Kun spillere kan bruge den kommando");
                throw new ConditionFailedException();
            }
            Player player = issuer.getPlayer();
            if(!player.isOp()) {
                if (!PermissionUtil.hasPermissionWithMessage(player, Permissions.ADMIN.getPermission(), null)) {
                    throw new ConditionFailedException();
                }
            }
        });

    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ChatInputListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
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

    public static ChatInputManager getChatInputManager() {
        return CHAT_INPUT_MANAGER;
    }
}
