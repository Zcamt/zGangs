package me.Zcamt.zgangs.objects.gang.allies;

import com.google.common.collect.ImmutableList;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GangAllies {
    private Gang gang;
    private int maxAllies;
    private int allyDamagePercent;
    private final List<UUID> alliedGangs;
    private final List<UUID> alliedGangInvitesIncoming;
    private final List<UUID> alliedGangInvitesOutgoing;

    public GangAllies(int maxAllies, int allyDamagePercent, List<UUID> alliedGangs, List<UUID> alliedGangInvitesIncoming, List<UUID> alliedGangInvitesOutgoing) {
        this.maxAllies = maxAllies;
        this.allyDamagePercent = allyDamagePercent;
        this.alliedGangs = alliedGangs;
        this.alliedGangInvitesIncoming = alliedGangInvitesIncoming;
        this.alliedGangInvitesOutgoing = alliedGangInvitesOutgoing;
    }


    public boolean addAlly(Gang newAlly) {
        if(gang.getGangRivals().isRival(newAlly.getUUID())) return false;
        if(gang.getGangRivals().isRivalAgainst(newAlly.getUUID())) return false;
        if(alliedGangs.size() >= maxAllies) return false;
        if (alliedGangs.contains(newAlly.getUUID())) return false;

        alliedGangs.add(newAlly.getUUID());
        newAlly.getGangAllies().addAlly(gang);
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
        if(gang.getGangRivals().isRival(ally.getUUID())) return false;
        if(gang.getGangRivals().isRivalAgainst(ally.getUUID())) return false;
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
        if(gang.getGangRivals().isRival(ally.getUUID())) return false;
        if(gang.getGangRivals().isRivalAgainst(ally.getUUID())) return false;
        if (alliedGangInvitesOutgoing.contains(ally.getUUID())) return false;

        alliedGangInvitesOutgoing.add(ally.getUUID());
        ally.getGangAllies().addAllyInviteIncoming(gang);

        gang.sendMessageToOnlineMembers(Messages.allyInviteOutgoing(ally.getName()));
        ally.sendMessageToOnlineMembers(Messages.allyInviteIncoming(gang.getName()));
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
        if(maxAllies < 1) {
            maxAllies = 1;
        }
        this.maxAllies = maxAllies;
        gang.serialize();
    }

    public void setAllyDamagePercent(int allyDamagePercent) {
        if(allyDamagePercent > 100) {
            allyDamagePercent = 100;
        } else if(allyDamagePercent < 0) {
            allyDamagePercent = 0;
        }
        this.allyDamagePercent = allyDamagePercent;
        gang.serialize();
    }

    public void setGang(Gang gang) {
        if(this.gang == null && gang != null) {
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

    public int getAllyCount() {
        return alliedGangs.size();
    }

    public int getMaxAllies() {
        return maxAllies;
    }

    public int getAllyDamagePercent() {
        return allyDamagePercent;
    }

    public List<UUID> getAlliedGangs() {
        return ImmutableList.copyOf(alliedGangs);
    }

    public List<UUID> getAlliedGangInvitesIncoming() {
        return ImmutableList.copyOf(alliedGangInvitesIncoming);
    }

    public List<UUID> getAlliedGangInvitesOutgoing() {
        return ImmutableList.copyOf(alliedGangInvitesOutgoing);
    }


    @NotNull
    public String toJson() {
        return ZGangs.GSON.toJson(this);
    }

}
