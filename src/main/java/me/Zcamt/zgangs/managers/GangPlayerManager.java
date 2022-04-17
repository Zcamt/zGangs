package me.Zcamt.zgangs.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import org.bson.Document;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GangPlayerManager {

    private final Database database;
    private final Cache<UUID, GangPlayer> gangPlayerCache;

    public GangPlayerManager(Database database) {
        this.database = database;
        this.gangPlayerCache = Caffeine.newBuilder()
                .maximumSize(1000L)
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .removalListener((RemovalListener<UUID, GangPlayer>) (integer, gangPlayer, cause) -> {
                    if(gangPlayer == null) return;
                    gangPlayer.serialize();
                }).build();
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

    private boolean idExistsInDatabase(UUID uuid){
        long count = database.getGangPlayerCollection().countDocuments(new Document("_id", uuid.toString()));
        return count > 0;
    }

    //Todo: kig i "JavaTests"

}
