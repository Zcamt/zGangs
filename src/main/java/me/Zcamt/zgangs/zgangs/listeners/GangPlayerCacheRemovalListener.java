package me.Zcamt.zgangs.zgangs.listeners;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.zgangs.objects.GangPlayer;
import me.Zcamt.zgangs.zgangs.objects.GangPlayerRepository;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public class GangPlayerCacheRemovalListener implements RemovalListener<UUID, GangPlayer> {

    private final GangPlayerRepository gangPlayerRepository;

    public GangPlayerCacheRemovalListener(GangPlayerRepository gangPlayerRepository) {
        this.gangPlayerRepository = gangPlayerRepository;
    }

    @Override
    public void onRemoval(@Nullable UUID uuid, @Nullable GangPlayer gangPlayer, RemovalCause removalCause) {
        gangPlayerRepository.updateGangPlayerInDB(gangPlayer);
    }
}
