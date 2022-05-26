package me.Zcamt.zgangs.objects.gang.gangstats;

import me.Zcamt.zgangs.objects.gang.Gang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GangStats {

    //Todo: Should be able to serialize after setting. Should be doable by copying the setup from GangAllies fx.
    private Gang gang;
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

        //Todo: possibly slightly dangerous, might need changing
        if(gang != null) {
            gang.serialize();
        }
    }

    public int getStatAmount(GangStat gangStat) {
        return stats.get(gangStat);
    }

    public Map<GangStat, Integer> getStatsMap() {
        return Collections.unmodifiableMap(stats);
    }


    public void setGang(Gang gang) {
        if(this.gang == null) {
            this.gang = gang;
        }
    }
}
