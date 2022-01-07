package me.Zcamt.zgangs.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.Zcamt.zgangs.listeners.GangPlayerCacheRemovalListener;
import me.Zcamt.zgangs.objects.CallbackGangPlayer;
import me.Zcamt.zgangs.objects.GangPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GangPlayerManager {

    private final DatabaseManager databaseManager;
    private final Cache<UUID, GangPlayer> gangPlayerCache;

    public GangPlayerManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        gangPlayerCache = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .removalListener(new GangPlayerCacheRemovalListener(this))
                .build();
    }

    private GangPlayer createNewGangPlayer(UUID uuid){
        if(isPlayerInDB(uuid)) {
            return getGangPlayer(uuid);
        }
        GangPlayer gangPlayer = new GangPlayer(uuid, 0, 0, new ArrayList<>());
        addToGangPlayerCache(uuid, gangPlayer);
        insertNewGangPlayerIntoDB(gangPlayer);
        return gangPlayer;
    }

    private GangPlayer createGangPlayerFromDB(UUID uuid){
        CallbackGangPlayer callbackGangPlayer = new CallbackGangPlayer();
        databaseManager.getGangPlayerFromDB(uuid, callbackGangPlayer);
        addToGangPlayerCache(uuid, callbackGangPlayer.getGangPlayer());
        return callbackGangPlayer.getGangPlayer();
    }

    private void insertNewGangPlayerIntoDB(GangPlayer gangPlayer){
        String query = "INSERT INTO " + databaseManager.getDatabase() + ".gang_players " +
                "(" +
                "UUID," +
                "GANG_ID, " +
                "GANG_RANK, " +
                "GANG_INVITES" +
                ")" +
                " " +
                "VALUES(" +
                "?," +
                "?," +
                "?," +
                "?" +
                ")";
        PreparedStatement ps = databaseManager.prepareStatement(query);
        try {
            ps.setString(1, gangPlayer.getUUID().toString());
            ps.setInt(2, gangPlayer.getGangID());
            ps.setInt(3, gangPlayer.getGangRank());
            ps.setString(4, gangPlayer.getSerializedGangInvitesList());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addToGangPlayerCache(UUID uuid, GangPlayer gangPlayer){
        gangPlayerCache.put(uuid, gangPlayer);
    }

    private boolean isPlayerInDB(UUID uuid){
        String query = "SELECT * FROM " + databaseManager + ".gang_players WHERE UUID = ?";
        PreparedStatement ps = databaseManager.prepareStatement(query);
        try {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            ps.close();
            //Todo: Might have to close RS here aswell, but not sure
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isPlayerInCache(UUID uuid){
        return gangPlayerCache.asMap().containsKey(uuid);
    }


    public GangPlayer getGangPlayer(UUID uuid){
        if(isPlayerInCache(uuid)){
            return gangPlayerCache.getIfPresent(uuid);
        } else if (isPlayerInDB(uuid)){
            return createGangPlayerFromDB(uuid);
        } else {
            return createNewGangPlayer(uuid);
        }
    }

    public void updateGangPlayerInDB(GangPlayer gangPlayer){
        String query = "UPDATE " + databaseManager.getDatabase() + ".gang_players SET " +
                "GANG_ID = ?, " +
                "GANG_RANK = ?, " +
                "GANG_INVITES = ?" +
                "" +
                "WHERE UUID = ?";
        PreparedStatement ps = databaseManager.prepareStatement(query);
        databaseManager.asyncThread(new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    ps.setInt(1, gangPlayer.getGangID());
                    ps.setInt(2, gangPlayer.getGangRank());
                    ps.setString(3, gangPlayer.getSerializedGangInvitesList());
                    ps.setString(4, gangPlayer.getUUID().toString());
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
