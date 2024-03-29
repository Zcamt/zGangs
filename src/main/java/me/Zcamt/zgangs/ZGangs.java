package me.Zcamt.zgangs;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.Zcamt.zgangs.chatinput.ChatInputManager;
import me.Zcamt.zgangs.commands.*;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.confirmaction.ConfirmActionManager;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.listeners.ChatInputListener;
import me.Zcamt.zgangs.listeners.InventoryListener;
import me.Zcamt.zgangs.listeners.NpcListener;
import me.Zcamt.zgangs.listeners.PlayerListener;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangAdapter;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.access.GangAccess;
import me.Zcamt.zgangs.objects.gang.access.GangAccessAdapter;
import me.Zcamt.zgangs.objects.gang.allies.GangAllies;
import me.Zcamt.zgangs.objects.gang.allies.GangAlliesAdapter;
import me.Zcamt.zgangs.objects.gang.itemdelivery.GangItemDelivery;
import me.Zcamt.zgangs.objects.gang.itemdelivery.GangItemDeliveryAdapter;
import me.Zcamt.zgangs.objects.gang.level.GangLevelManager;
import me.Zcamt.zgangs.objects.gang.members.GangMembers;
import me.Zcamt.zgangs.objects.gang.members.GangMembersAdapter;
import me.Zcamt.zgangs.objects.gang.motd.GangMotd;
import me.Zcamt.zgangs.objects.gang.motd.GangMotdAdapter;
import me.Zcamt.zgangs.objects.gang.permissions.GangPermissions;
import me.Zcamt.zgangs.objects.gang.permissions.GangPermissionsAdapter;
import me.Zcamt.zgangs.objects.gang.rivals.GangRivals;
import me.Zcamt.zgangs.objects.gang.rivals.GangRivalsAdapter;
import me.Zcamt.zgangs.objects.gang.stats.GangStats;
import me.Zcamt.zgangs.objects.gang.stats.GangStatsAdapter;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerAdapter;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.objects.gangplayer.settings.GangPlayerSettings;
import me.Zcamt.zgangs.objects.gangplayer.settings.GangPlayerSettingsAdapter;
import me.Zcamt.zgangs.objects.leaderboard.LeaderboardManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.PermissionUtil;
import me.Zcamt.zgangs.utils.Permissions;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZGangs extends JavaPlugin {

    //Todo: Admin commands

    //Todo: Add confirm action system in the places where it is needed

    //Todo: Could add information in infoGUI about how gangdamage and allydamage work

    //Todo: Add "previous menu" as a parameter for menus to allow for precise and correct back-tracking via the back button.
    // Null would be "close menu", shouldn't be the exact last instance but open a new instance of previous menu.

    //Todo: Change use of "unexpectedError" message in Messages class, is currently used for everything and shouldn't be.

    //Todo: Checks for adapter variables being null
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Gang.class, new GangAdapter())
            .registerTypeAdapter(GangAllies.class, new GangAlliesAdapter())
            .registerTypeAdapter(GangRivals.class, new GangRivalsAdapter())
            .registerTypeAdapter(GangMembers.class, new GangMembersAdapter())
            .registerTypeAdapter(GangStats.class, new GangStatsAdapter())
            .registerTypeAdapter(GangMotd.class, new GangMotdAdapter())
            .registerTypeAdapter(GangPermissions.class, new GangPermissionsAdapter())
            .registerTypeAdapter(GangItemDelivery.class, new GangItemDeliveryAdapter())
            .registerTypeAdapter(GangAccess.class, new GangAccessAdapter())
            .registerTypeAdapter(GangPlayer.class, new GangPlayerAdapter())
            .registerTypeAdapter(GangPlayerSettings.class, new GangPlayerSettingsAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(2);
    private static final Database DATABASE = new Database();
    private static final GangManager GANG_MANAGER = new GangManager();
    private static final GangPlayerManager GANG_PLAYER_MANAGER = new GangPlayerManager();
    private static final LeaderboardManager LEADERBOARD_MANAGER = new LeaderboardManager();
    private static final GangLevelManager GANG_LEVEL_MANAGER = new GangLevelManager();
    private static final ChatInputManager CHAT_INPUT_MANAGER = new ChatInputManager();
    private static final ConfirmActionManager CONFIRM_ACTION_MANAGER = new ConfirmActionManager();
    private static Economy ECONOMY;
    private static LuckPerms LUCKPERMS;

    @Override
    public void onEnable() {
        loadConfig();
        registerCommands();
        registerListeners();
        if(!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if(provider != null) {
            LUCKPERMS = provider.getProvider();
        } else {
            getLogger().severe("Disabled due to no Luckperms dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            getLeaderboardManager().updateAllLeaderBoards();
        }, 6000L, 72000L);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        GANG_PLAYER_MANAGER.invalidateCache();
        GANG_MANAGER.invalidateCache();
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
        commandManager.registerCommand(new GangChatCommand());
        commandManager.registerCommand(new AllyChatCommand());

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
            if (!PermissionUtil.hasPermissionWithMessage(player, Permissions.PLAYER.getPermission(),
                    "&cFejl: Kun spillere kan bruge den kommando")) {
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
                if (!PermissionUtil.hasPermissionWithMessage(player, Permissions.ADMIN.getPermission(),
                        "&cFejl: Kun admins kan bruge den kommando")) {
                    throw new ConditionFailedException();
                }
            }
        });

    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ChatInputListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new NpcListener(), this);
    }

    private boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        ECONOMY = rsp.getProvider();
        return ECONOMY != null;
    }

    public static ExecutorService getThreadPool() {
        return THREAD_POOL;
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

    public static ConfirmActionManager getConfirmActionManager() {
        return CONFIRM_ACTION_MANAGER;
    }

    public static Economy getEconomy() {
        return ECONOMY;
    }

    public static LuckPerms getLuckperms() {
        return LUCKPERMS;
    }
}
