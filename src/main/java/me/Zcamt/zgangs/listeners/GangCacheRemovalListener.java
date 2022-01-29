package me.Zcamt.zgangs.listeners;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.objects.Gang;
import me.Zcamt.zgangs.objects.GangRepository;
import org.checkerframework.checker.nullness.qual.Nullable;

public class GangCacheRemovalListener implements RemovalListener<Integer, Gang> {

    private final GangRepository gangRepository;

    public GangCacheRemovalListener(GangRepository gangRepository) {
        this.gangRepository = gangRepository;
    }

    @Override
    public void onRemoval(@Nullable Integer integer, @Nullable Gang gang, RemovalCause removalCause) {
        gangRepository.updateGangInDB(gang);
    }
}
