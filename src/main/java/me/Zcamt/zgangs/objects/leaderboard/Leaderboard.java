package me.Zcamt.zgangs.objects.leaderboard;

import java.util.HashMap;
import java.util.UUID;

public interface Leaderboard {

    void update();
    HashMap<UUID, Integer> getLeaderBoard();

}
