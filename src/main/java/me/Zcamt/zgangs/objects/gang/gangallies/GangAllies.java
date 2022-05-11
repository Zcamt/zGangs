package me.Zcamt.zgangs.objects.gang.gangallies;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GangAllies {
    private Gang gang;
    private int maxAllies;
    private final List<UUID> alliedGangs;
    private final List<UUID> alliedGangInvitesIncoming;
    private final List<UUID> alliedGangInvitesOutgoing;

    public GangAllies(int maxAllies, List<UUID> alliedGangs, List<UUID> alliedGangInvitesIncoming, List<UUID> alliedGangInvitesOutgoing) {
        this.maxAllies = maxAllies;
        this.alliedGangs = alliedGangs;
        this.alliedGangInvitesIncoming = alliedGangInvitesIncoming;
        this.alliedGangInvitesOutgoing = alliedGangInvitesOutgoing;
    }


    public boolean addAlly(Gang ally) {
        if(alliedGangs.size() >= maxAllies) return false;
        if (alliedGangs.contains(ally.getUUID())) return false;

        alliedGangs.add(ally.getUUID());
        ally.getGangAllies().addAlly(gang);
        gang.serialize();
        return true;
    }

    public boolean removeAlly(Gang ally) {
        if (!alliedGangs.contains(ally.getUUID())) return false;

        alliedGangs.remove(ally.getUUID());
        ally.getGangAllies().removeAlly(gang);
        gang.serialize();
        return true;
    }

    public boolean addAllyInviteIncoming(Gang ally) {
        if (alliedGangInvitesIncoming.contains(ally.getUUID())) return false;

        alliedGangInvitesIncoming.add(ally.getUUID());
        ally.getGangAllies().addAllyInviteOutgoing(gang);
        gang.serialize();
        return true;
    }

    public boolean removeAllyInviteIncoming(Gang ally) {
        if (!alliedGangInvitesIncoming.contains(ally.getUUID())) return false;

        alliedGangInvitesIncoming.remove(ally.getUUID());
        ally.getGangAllies().removeAllyInviteOutgoing(gang);
        gang.serialize();
        return true;
    }

    public boolean addAllyInviteOutgoing(Gang ally) {
        if (alliedGangInvitesOutgoing.contains(ally.getUUID())) return false;

        alliedGangInvitesOutgoing.add(ally.getUUID());
        ally.getGangAllies().addAllyInviteIncoming(gang);
        gang.serialize();
        return true;
    }

    public boolean removeAllyInviteOutgoing(Gang ally) {
        if (!alliedGangInvitesOutgoing.contains(ally.getUUID())) return false;

        alliedGangInvitesOutgoing.remove(ally.getUUID());
        ally.getGangAllies().removeAllyInviteIncoming(gang);
        gang.serialize();
        return true;
    }

    public void setMaxAllies(int maxAllies) {
        this.maxAllies = maxAllies;
        gang.serialize();
    }

    public void setGang(Gang gang) {
        if(gang != null) {
            this.gang = gang;
        }
    }



    public boolean isAllied(UUID uuid) {
        return alliedGangs.contains(uuid);
    }

    public boolean incomingInvitesContains(UUID uuid) {
        return alliedGangInvitesIncoming.contains(uuid);
    }

    public boolean outgoingInvitesContains(UUID uuid) {
        return alliedGangInvitesOutgoing.contains(uuid);
    }

    public int getMaxAllies() {
        return maxAllies;
    }

    public List<UUID> getAlliedGangs() {
        return Collections.unmodifiableList(alliedGangs);
    }

    public List<UUID> getAlliedGangInvitesIncoming() {
        return Collections.unmodifiableList(alliedGangInvitesIncoming);
    }

    public List<UUID> getAlliedGangInvitesOutgoing() {
        return Collections.unmodifiableList(alliedGangInvitesOutgoing);
    }


    @NotNull
    public String toJson() {
        return ZGangs.GSON.toJson(this);
    }

}
