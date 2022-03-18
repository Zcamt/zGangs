package me.Zcamt.zgangs.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.objects.Gang;
import org.bson.Document;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GangManager {

    private final Database database;
    private final Cache<UUID, Gang> gangCache;

    public GangManager(Database database) {
        this.database = database;
        this.gangCache = Caffeine.newBuilder()
                .maximumSize(1000L)
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .removalListener((RemovalListener<UUID, Gang>) (uuid, gang, cause) -> {
                    if(gang == null) return;
                    gang.serialize();
                }).build();
    }

    //Todo: Gør alt database stuffs ASYNC
    public Gang findById(UUID uuid){
        if(gangCache.asMap().containsKey(uuid)){
            return gangCache.getIfPresent(uuid);
        }
        Document gangDocument = database.getGangCollection().find(new Document("_id", uuid.toString())).first();
        if(gangDocument == null) {
            throw new NoSuchElementException("Couldn't find gang with UUID '" + uuid + "'");
        }
        Gang gang = ZGangs.GSON.fromJson(gangDocument.toJson(), Gang.class);
        addGangToCache(uuid, gang);
        return gang;
    }

    private boolean isIdInCache(UUID uuid){
        return gangCache.asMap().containsKey(uuid);
    }

    private void addGangToCache(UUID uuid, Gang gang){
        gangCache.put(uuid, gang);
    }

    //Todo: kig i "JavaTests"
}
