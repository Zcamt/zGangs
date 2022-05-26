package me.Zcamt.zgangs.objects.gang.gangrivals;

import me.Zcamt.zgangs.objects.gang.Gang;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GangRivals {
    private Gang gang;
    private final List<UUID> rivalGangs;
    private final List<UUID> rivalGangsAgainst;

    public GangRivals(List<UUID> rivalGangs, List<UUID> rivalGangsAgainst) {
        this.rivalGangs = rivalGangs;
        this.rivalGangsAgainst = rivalGangsAgainst;
    }

    public boolean addRival(Gang rival) {
        if (rivalGangs.contains(rival.getUUID())) return false;

        rivalGangs.add(rival.getUUID());
        rival.getGangRivals().addRivalAgainst(gang);
        gang.serialize();
        return true;
    }

    public boolean removeRival(Gang rival) {
        if (!rivalGangs.contains(rival.getUUID())) return false;

        rivalGangs.remove(rival.getUUID());
        rival.getGangRivals().removeRivalAgainst(gang);
        gang.serialize();
        return true;
    }

    public boolean addRivalAgainst(Gang rival) {
        if (rivalGangsAgainst.contains(rival.getUUID())) return false;

        rivalGangsAgainst.add(rival.getUUID());
        rival.getGangRivals().addRival(gang);
        gang.serialize();
        return true;
    }

    public boolean removeRivalAgainst(Gang rival) {
        if (!rivalGangsAgainst.contains(rival.getUUID())) return false;

        rivalGangsAgainst.remove(rival.getUUID());
        rival.getGangRivals().removeRival(rival);
        gang.serialize();
        return true;
    }


    public boolean isRival(UUID gangUUID) {
        return rivalGangs.contains(gangUUID);
    }

    public boolean isRivalAgainst(UUID gangUUID) {
        return rivalGangsAgainst.contains(gangUUID);
    }

    public List<UUID> getRivalGangs() {
        return Collections.unmodifiableList(rivalGangs);
    }

    public List<UUID> getRivalGangsAgainst() {
        return Collections.unmodifiableList(rivalGangsAgainst);
    }

    public void setGang(Gang gang) {
        if(gang != null) {
            this.gang = gang;
        }
    }

}
