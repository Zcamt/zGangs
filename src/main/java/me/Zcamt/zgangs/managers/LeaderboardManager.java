package me.Zcamt.zgangs.managers;

import me.Zcamt.zgangs.objects.leaderboard.KillsLeaderboard;

public class LeaderboardManager {

    private final KillsLeaderboard killsLeaderboard;

    public LeaderboardManager() {
        this.killsLeaderboard = new KillsLeaderboard();
    }

    public void updateAllLeaderBoards(){
        killsLeaderboard.update();
    }


    public KillsLeaderboard getKillsLeaderboard() {
        return killsLeaderboard;
    }
}
