package me.Zcamt.zgangs.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.Zcamt.zgangs.objects.GangPlayer;
import me.Zcamt.zgangs.listeners.GangPlayerCacheRemovalListener;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GangPlayerManager {

    //Todo: Adapt to the same way with GangManager and GangRepository

    private final Database database;
    private final Cache<UUID, GangPlayer> gangPlayerCache;

    public GangPlayerManager(Database database) {
        this.database = database;
        gangPlayerCache = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .removalListener(new GangPlayerCacheRemovalListener(database.getGangPlayerRepository()))
                .build();
    }

    private GangPlayer gangPlayerFromDB(UUID uuid) {
        CompletableFuture<GangPlayer> gangPlayerCompletableFuture = database.getGangPlayerRepository().getGangPlayerFromDB(uuid);
        try {
            GangPlayer gangPlayer = gangPlayerCompletableFuture.get();
            addToGangPlayerCache(uuid, gangPlayer);
            return gangPlayer;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to retrieve gang player with UUID '" + uuid + "' from Database " + e);
        }
    }

    private GangPlayer createNewGangPlayer(UUID uuid) throws ExecutionException, InterruptedException {
        //Add try catch instead of method throwing exceptions
        if(database.getGangPlayerRepository().uuidExistsInDB(uuid).get()) {
            return getGangPlayer(uuid);
        }
        GangPlayer gangPlayer = new GangPlayer(uuid, 0, 0, new ArrayList<>(), database.getGangPlayerRepository());
        addToGangPlayerCache(uuid, gangPlayer);
        database.getGangPlayerRepository().insertNewGangPlayerIntoDB(gangPlayer);
        return gangPlayer;
    }

    private void addToGangPlayerCache(UUID uuid, GangPlayer gangPlayer){
        gangPlayerCache.put(uuid, gangPlayer);
    }

    private boolean isPlayerInCache(UUID uuid){
        return gangPlayerCache.asMap().containsKey(uuid);
    }

    public GangPlayer getGangPlayer(UUID uuid){
        try {
            if (isPlayerInCache(uuid)) {
                return gangPlayerCache.getIfPresent(uuid);
            } else if (database.getGangPlayerRepository().uuidExistsInDB(uuid).get()) {
                return gangPlayerFromDB(uuid);
            } else {
                return createNewGangPlayer(uuid);
            }
        } catch (ExecutionException | InterruptedException e) {
            //Todo: Add message
            throw new RuntimeException(e);
        }
    }

}
