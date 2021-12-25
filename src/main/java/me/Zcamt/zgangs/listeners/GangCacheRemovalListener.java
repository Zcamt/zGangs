package me.Zcamt.zgangs.listeners;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.managers.GangManager;
import me.Zcamt.zgangs.objects.Gang;
import org.checkerframework.checker.nullness.qual.Nullable;

public class GangCacheRemovalListener implements RemovalListener<Integer, Gang> {

    private final GangManager gangManager;

    public GangCacheRemovalListener(GangManager gangManager) {
        this.gangManager = gangManager;
    }

    @Override
    public void onRemoval(@Nullable Integer integer, @Nullable Gang gang, RemovalCause removalCause) {
        gangManager.updateGangInDB(gang);
    }
}
