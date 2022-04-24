package me.Zcamt.zgangs.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangPermissions;
import org.bson.Document;

import java.util.*;
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

    public Gang createNewGang(String name, UUID ownerUUID) {
        UUID uuid = UUID.randomUUID();

        while (idExistsInDatabase(uuid)) {
            uuid = UUID.randomUUID();
        }

        List<UUID> memberList = new ArrayList<>();
        memberList.add(ownerUUID);

        Gang gang = new Gang(uuid, System.currentTimeMillis(), name, 1, 0, 0, 0,
                Config.defaultMaxMembers, Config.defaultMaxAllies, ownerUUID, memberList,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new GangPermissions(new HashMap<>()));

        return gang;
    }

    //Todo: Delete gang method
    // Tjek på om den er tom - så man kun kan slette en bande hvis man er alene i den

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
        addGangToCache(gang.getUUID(), gang);
        return gang;
    }

    public boolean nameExistsInDatabase(String name){
        long count = database.getGangCollection().countDocuments(new Document("name", name));
        return count > 0;
    }

    private boolean isIdInCache(UUID uuid){
        return gangCache.asMap().containsKey(uuid);
    }

    private void addGangToCache(UUID uuid, Gang gang){
        gangCache.put(uuid, gang);
    }

    private boolean idExistsInDatabase(UUID uuid){
        long count = database.getGangCollection().countDocuments(new Document("_id", uuid.toString()));
        return count > 0;
    }

    //Todo: kig i "JavaTests"
}
