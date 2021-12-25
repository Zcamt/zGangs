package me.Zcamt.zgangs.listeners;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.managers.GangPlayerManager;
import me.Zcamt.zgangs.objects.GangPlayer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public class GangPlayerCacheRemovalListener implements RemovalListener<UUID, GangPlayer> {

    private final GangPlayerManager gangPlayerManager;

    public GangPlayerCacheRemovalListener(GangPlayerManager gangPlayerManager) {
        this.gangPlayerManager = gangPlayerManager;
    }

    @Override
    public void onRemoval(@Nullable UUID uuid, @Nullable GangPlayer gangPlayer, RemovalCause removalCause) {
        gangPlayerManager.updateGangPlayerInDB(gangPlayer);
    }
}
