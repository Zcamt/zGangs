package me.Zcamt.zgangs.zgangs.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.Zcamt.zgangs.zgangs.listeners.GangCacheRemovalListener;
import me.Zcamt.zgangs.zgangs.objects.Gang;
import me.Zcamt.zgangs.zgangs.objects.GangPlayer;
import me.Zcamt.zgangs.zgangs.utils.Utilities;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GangManager {

    private final Database database;
    private final Cache<Integer, Gang> gangCache;

    public GangManager(Database database) {
        this.database = database;
        gangCache = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .removalListener(new GangCacheRemovalListener(database.getGangRepository()))
                .build();
    }

    private Gang gangFromDB(int gangID) {
        CompletableFuture<Gang> gangCompletableFuture = database.getGangRepository().getGangFromDB(gangID);
        try {
            Gang gang = gangCompletableFuture.get();
            addToGangCache(gangID, gang);
            return gang;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to retrieve gang with ID '" + gangID + "' from Database " + e);
        }
    }

    private boolean isGangInCache(int gangID) {
        return gangCache.asMap().containsKey(gangID);
    }

    private void addToGangCache(int gangID, Gang gang){
        gangCache.put(gangID, gang);
    }

    public Gang createNewGang(GangPlayer gangPlayer, String name) {
        //Todo check if player is in gang
        boolean gangNameValid = !Utilities.isGangNameValid(name, database);
        if(gangNameValid) {
            if(gangPlayer.getOfflinePlayer().isOnline()) {
                Utilities.sendMessage((Player) gangPlayer.getOfflinePlayer(), "&cYou cannot call your new gang that!");
            }
            return null;
        }
        int gangID;
        try {
            gangID = database.getGangRepository().getNextGangID();
        } catch (SQLException e) {
            throw new RuntimeException("Wasn't able to find the next gang-id available " + e);
        }
        HashMap<UUID, Integer> memberList = new HashMap<>();
        memberList.put(gangPlayer.getUUID(), 5);
        gangPlayer.setGangID(gangID);
        gangPlayer.setGangRank(5);
        //Todo: Update player perhaps?

        Gang gang = new Gang(gangID, name, 1, 0, 0, 0, gangPlayer.getUUID(), memberList, new ArrayList<>());
        addToGangCache(gang.getId(), gang);
        database.getGangRepository().insertNewGangIntoDB(gang);
        return gang;
    }

    public Gang getGang(GangPlayer gangPlayer) throws ExecutionException, InterruptedException {
        //Add try catch instead of method throwing exceptions
        if(gangPlayer.getGangID() == 0) return null;
        if(isGangInCache(gangPlayer.getGangID())) {
            return gangCache.getIfPresent(gangPlayer.getGangID());
        } else if (database.getGangRepository().gangIdExists(gangPlayer.getGangID()).get()){
            Gang gang = gangFromDB(gangPlayer.getGangID());
            return gang;
        } else {
            return null;
        }
    }
    //Todo: Implement getGang from ID
}
