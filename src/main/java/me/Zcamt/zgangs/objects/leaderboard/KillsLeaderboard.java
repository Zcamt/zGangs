package me.Zcamt.zgangs.objects.leaderboard;

import java.util.HashMap;
import java.util.UUID;

public class KillsLeaderboard implements Leaderboard {
    private final HashMap<Integer, LeaderboardEntry> leaderboard = new HashMap<>();

    @Override
    public void update() {

    }

    @Override
    public HashMap<UUID, Integer> getLeaderBoard() {
        return null;
    }
}
