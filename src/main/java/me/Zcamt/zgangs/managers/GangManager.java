package me.Zcamt.zgangs.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.Zcamt.zgangs.helpers.Utilities;
import me.Zcamt.zgangs.listeners.GangCacheRemovalListener;
import me.Zcamt.zgangs.objects.CallbackGang;
import me.Zcamt.zgangs.objects.Gang;
import me.Zcamt.zgangs.objects.GangPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GangManager {

    private final DatabaseManager databaseManager;
    private final Cache<Integer, Gang> gangCache;

    public GangManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        gangCache = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .removalListener(new GangCacheRemovalListener(this))
                .build();
    }

    private Gang createGangFromDB(int gangID){
        CallbackGang callbackGang = new CallbackGang();
        databaseManager.getGangFromDB(gangID, callbackGang);
        addToGangCache(gangID, callbackGang.getGang());
        return callbackGang.getGang();
    }

    private boolean isGangInCache(int gangID) {
        return gangCache.asMap().containsKey(gangID);
    }

    private void insertNewGangIntoDB(Gang gang){
        String query = "INSERT INTO " + databaseManager.getDatabase() + ".gangs " +
                "(" +
                "ID," +
                "NAME, " +
                "LEVEL, " +
                "KILLS, " +
                "DEATHS, " +
                "BANK, " +
                "OWNER_UUID, " +
                "MEMBERS, " +
                "CREATED_DATE" +
                ")" +
                " " +
                "VALUES(" +
                "DEFAULT," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "DEFAULT" +
                ")";
        PreparedStatement ps = databaseManager.prepareStatement(query);
        databaseManager.asyncThread(new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    ps.setString(1, gang.getName());
                    ps.setInt(2, gang.getLevel());
                    ps.setInt(3, gang.getKills());
                    ps.setInt(4, gang.getDeaths());
                    ps.setInt(5, gang.getBank());
                    ps.setString(6, gang.getOwnerUUID().toString());
                    ps.setString(7, gang.getSerializedMemberList());

                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void addToGangCache(int gangID, Gang gang){
        gangCache.put(gangID, gang);
    }


    public boolean isGangIDInDB(int gangID){
        String query = "SELECT * FROM " + databaseManager + ".gangs WHERE ID = ?";
        PreparedStatement ps = databaseManager.prepareStatement(query);
        try {
            ps.setInt(1, gangID);
            ResultSet rs = ps.executeQuery();
            ps.close();
            //Todo: Might have to close RS here aswell, but not sure
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isGangNameInDB(String gangName){
        String query = "SELECT * FROM " + databaseManager + ".gangs WHERE NAME = ?";
        PreparedStatement ps = databaseManager.prepareStatement(query);
        try {
            ps.setString(1, gangName);
            ResultSet rs = ps.executeQuery();
            ps.close();
            //Todo: Might have to close RS here aswell, but not sure
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Gang createNewGang(GangPlayer gangPlayer, String name) {
        //Todo check if player is in gang
        if(!Utilities.isGangNameValid(name, this)) {
            Utilities.sendMessage(gangPlayer.getPlayer(), "&cYou cannot call your new gang that!");
            return null;
        }
        int gangID;
        try {
            gangID = databaseManager.getNextGangID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        HashMap<UUID, Integer> memberList = new HashMap<>();
        memberList.put(gangPlayer.getPlayer().getUniqueId(), 5);
        //Todo: Update gangplayer
        gangPlayer.setGangID(gangID);
        gangPlayer.setGangRank(5);

        Gang gang = new Gang(gangID, name, 1, 0, 0, 0, gangPlayer.getPlayer().getUniqueId(), memberList);
        addToGangCache(gang.getId(), gang);
        insertNewGangIntoDB(gang);
        return gang;
    }

    public Gang getGang(GangPlayer gangPlayer){
        if(gangPlayer.getGangID() == 0) return null;
        if(isGangInCache(gangPlayer.getGangID())) {
            return gangCache.getIfPresent(gangPlayer.getGangID());
        } else if (isGangIDInDB(gangPlayer.getGangID())){
            Gang gang = createGangFromDB(gangPlayer.getGangID());
            return gang;
        } else {
            return null;
        }
    }
    //Todo: Implement getGang from ID

    public void updateGangInDB(Gang gang){
        String query = "UPDATE " + databaseManager.getDatabase() + ".gangs SET " +
                "NAME = ?, " +
                "LEVEL = ?, " +
                "KILLS = ?, " +
                "DEATHS = ?, " +
                "BANK = ?, " +
                "OWNER_UUID = ?, " +
                "MEMBERS = ? " +
                "" +
                "WHERE ID = ?";
        PreparedStatement ps = databaseManager.prepareStatement(query);
        databaseManager.asyncThread(new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    ps.setString(1, gang.getName());
                    ps.setInt(2, gang.getLevel());
                    ps.setInt(3, gang.getKills());
                    ps.setInt(4, gang.getDeaths());
                    ps.setInt(5, gang.getBank());
                    ps.setString(6, gang.getOwnerUUID().toString());
                    ps.setString(7, gang.getSerializedMemberList());
                    ps.setInt(8, gang.getId());

                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
