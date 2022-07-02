package me.Zcamt.zgangs.objects.leaderboard;

import me.Zcamt.zgangs.objects.leaderboard.leaderboards.*;

public class LeaderboardManager {

    private final KillsLeaderboard killsLeaderboard;
    private final GuardKillsLeaderboard guardKillsLeaderboard;
    private final OfficerPlusLeaderboard officerPlusLeaderboard;
    private final DeathLeaderboard deathLeaderboard;
    private final BankLeaderboard bankLeaderboard;
    private final LevelLeaderboard levelLeaderboard;

    public LeaderboardManager() {
        this.killsLeaderboard = new KillsLeaderboard();
        this.guardKillsLeaderboard = new GuardKillsLeaderboard();
        this.officerPlusLeaderboard = new OfficerPlusLeaderboard();
        this.deathLeaderboard = new DeathLeaderboard();
        this.bankLeaderboard = new BankLeaderboard();
        this.levelLeaderboard = new LevelLeaderboard();

        updateAllLeaderBoards();
    }

    public void updateAllLeaderBoards(){
        killsLeaderboard.update();
        guardKillsLeaderboard.update();
        officerPlusLeaderboard.update();
        deathLeaderboard.update();
        bankLeaderboard.update();
        levelLeaderboard.update();
    }

    public Leaderboard getLeaderboardFromType(LeaderboardType leaderboardType) {
        switch (leaderboardType) {
            default:
            case KILLS:
                return getKillsLeaderboard();
            case GUARD_KILLS:
                return getGuardKillsLeaderboard();
            case OFFICER_PLUS_KILLS:
                return getOfficerPlusLeaderboard();
            case DEATHS:
                return getDeathLeaderboard();
            case BANK:
                return getBankLeaderboard();
            case LEVEL:
                return getLevelLeaderboard();
        }
    }

    public KillsLeaderboard getKillsLeaderboard() {
        return killsLeaderboard;
    }

    public GuardKillsLeaderboard getGuardKillsLeaderboard() {
        return guardKillsLeaderboard;
    }

    public OfficerPlusLeaderboard getOfficerPlusLeaderboard() {
        return officerPlusLeaderboard;
    }

    public DeathLeaderboard getDeathLeaderboard() {
        return deathLeaderboard;
    }

    public BankLeaderboard getBankLeaderboard() {
        return bankLeaderboard;
    }

    public LevelLeaderboard getLevelLeaderboard() {
        return levelLeaderboard;
    }
}
