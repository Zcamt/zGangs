package me.Zcamt.zgangs.objects.gang.stats;

import me.Zcamt.zgangs.objects.gang.Gang;

public class GangStats {

    private Gang gang;
    private int kills;
    private int deaths;
    private int guard_kills_in_c;
    private int guard_kills_in_b;
    private int guard_kills_in_a;
    private int officer_plus_kills;

    public GangStats(int kills, int deaths, int guard_kills_in_c, int guard_kills_in_b, int guard_kills_in_a, int officer_plus_kills) {
        this.kills = kills;
        this.deaths = deaths;
        this.guard_kills_in_c = guard_kills_in_c;
        this.guard_kills_in_b = guard_kills_in_b;
        this.guard_kills_in_a = guard_kills_in_a;
        this.officer_plus_kills = officer_plus_kills;
    }


    public void setKills(int kills) {
        this.kills = kills;
        gang.serialize();
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        gang.serialize();
    }

    public void setGuard_kills_in_c(int guard_kills_in_c) {
        this.guard_kills_in_c = guard_kills_in_c;
        gang.serialize();
    }

    public void setGuard_kills_in_b(int guard_kills_in_b) {
        this.guard_kills_in_b = guard_kills_in_b;
        gang.serialize();
    }

    public void setGuard_kills_in_a(int guard_kills_in_a) {
        this.guard_kills_in_a = guard_kills_in_a;
        gang.serialize();
    }

    public void setOfficer_plus_kills(int officer_plus_kills) {
        this.officer_plus_kills = officer_plus_kills;
        gang.serialize();
    }



    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getGuard_kills_in_c() {
        return guard_kills_in_c;
    }

    public int getGuard_kills_in_b() {
        return guard_kills_in_b;
    }

    public int getGuard_kills_in_a() {
        return guard_kills_in_a;
    }

    public int getOfficer_plus_kills() {
        return officer_plus_kills;
    }

    public void setGang(Gang gang) {
        if(this.gang == null && gang != null) {
            this.gang = gang;
        }
    }
}
