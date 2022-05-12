package me.Zcamt.zgangs.objects.gang.gangstats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GangStats {

    private final Map<GangStat, Integer> stats = new HashMap<>();

    public GangStats(Map<GangStat, Integer> statsMap) {
        for (GangStat gangStat : GangStat.values()) {
            setStat(gangStat, statsMap.getOrDefault(gangStat, 0));
        }
    }

    public void setStat(GangStat gangStat, int amount) {
        if(amount < 0) {
            amount = 1;
        }
        stats.put(gangStat, amount);
    }

    public int getStatAmount(GangStat gangStat) {
        return stats.get(gangStat);
    }

    public Map<GangStat, Integer> getStatsMap() {
        return Collections.unmodifiableMap(stats);
    }

}
