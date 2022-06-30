package me.Zcamt.zgangs.objects.leaderboard;

public class LeaderboardManager {

    private final KillsLeaderboard killsLeaderboard;

    public LeaderboardManager() {
        this.killsLeaderboard = new KillsLeaderboard();
    }

    public void updateAllLeaderBoards(){
        killsLeaderboard.update();
    }

    public Leaderboard getLeaderboardFromType(LeaderboardType leaderboardType) {
        switch (leaderboardType) {
            default:
            case KILLS:
                return getKillsLeaderboard();
        }
    }

    public KillsLeaderboard getKillsLeaderboard() {
        return killsLeaderboard;
    }
}
