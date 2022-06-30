package me.Zcamt.zgangs.objects.leaderboard;

import java.util.List;

public abstract class Leaderboard {

    private long lastUpdatedEpoch = 0;

    public abstract void update();

    public abstract List<GangLeaderboardEntry> getLeaderboard();

    public long getLastUpdatedEpoch() {
        return lastUpdatedEpoch;
    }

    public void setLastUpdatedEpoch(long lastUpdatedEpoch) {
        this.lastUpdatedEpoch = lastUpdatedEpoch;
    }
}
