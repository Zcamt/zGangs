package me.Zcamt.zgangs.objects.gangplayer;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.objects.gangplayer.settings.GangPlayerSettings;
import org.bson.Document;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GangPlayerManager {

    private final Database database = ZGangs.getDatabase();
    private final Cache<UUID, GangPlayer> gangPlayerCache;

    public GangPlayerManager() {
        this.gangPlayerCache = Caffeine.newBuilder()
                .maximumSize(1000L)
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .removalListener((RemovalListener<UUID, GangPlayer>) (integer, gangPlayer, cause) -> {
                    if(gangPlayer == null) return;
                    gangPlayer.serialize();
                }).build();
    }

    public GangPlayer createNewGangPlayer(UUID uuid){
        GangPlayer gangPlayer = new GangPlayer(uuid, null, GangRank.RECRUIT, new ArrayList<>(),
                new GangPlayerSettings(
                        true,
                        true,
                        true,
                        true,
                        true,
                        true));
        addGangPlayerToCache(uuid, gangPlayer);
        gangPlayer.serialize();
        return gangPlayer;
    }

    //Todo: Gør alt database stuffs ASYNC
    public GangPlayer findById(UUID uuid){
        if(gangPlayerCache.asMap().containsKey(uuid)){
            return gangPlayerCache.getIfPresent(uuid);
        }
        Document gangDocument = database.getGangPlayerCollection().find(new Document("_id", uuid.toString())).first();
        if(gangDocument == null) {
            throw new NoSuchElementException("Couldn't find gangplayer with UUID '" + uuid + "'");
        }
        GangPlayer gangPlayer = ZGangs.GSON.fromJson(gangDocument.toJson(), GangPlayer.class);
        addGangPlayerToCache(gangPlayer.getUUID(), gangPlayer);
        return gangPlayer;
    }

    private boolean isIdInCache(UUID uuid){
        return gangPlayerCache.asMap().containsKey(uuid);
    }

    private void addGangPlayerToCache(UUID uuid, GangPlayer gangPlayer){
        gangPlayerCache.put(uuid, gangPlayer);
    }

    public boolean idExistsInDatabase(UUID uuid){
        long count = database.getGangPlayerCollection().countDocuments(new Document("_id", uuid.toString()));
        return count > 0;
    }

    public void invalidatePlayer(UUID uuid) {
        gangPlayerCache.invalidate(uuid);
    }

    public void invalidateCache() {
        gangPlayerCache.invalidateAll();
    }
}
