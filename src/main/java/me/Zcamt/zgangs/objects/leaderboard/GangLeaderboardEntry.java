package me.Zcamt.zgangs.objects.leaderboard;

import java.util.UUID;

public class GangLeaderboardEntry {
    private final UUID gangUuid;
    private final UUID gangOwner;
    private final LeaderboardType leaderboardType;
    private final int amount;

    public GangLeaderboardEntry(UUID gangUuid, UUID gangOwner, LeaderboardType leaderboardType, int amount) {
        this.gangUuid = gangUuid;
        this.gangOwner = gangOwner;
        this.leaderboardType = leaderboardType;
        this.amount = amount;
    }

    public UUID getGangUuid() {
        return gangUuid;
    }

    public UUID getGangOwner() {
        return gangOwner;
    }

    public LeaderboardType getLeaderboardType() {
        return leaderboardType;
    }

    public int getAmount() {
        return amount;
    }
}
