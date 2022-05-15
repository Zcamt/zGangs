package me.Zcamt.zgangs.objects.leaderboard;

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
