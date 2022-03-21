package me.Zcamt.zgangs.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.objects.GangPlayer;

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
    //Todo: kig i "JavaTests"

}
