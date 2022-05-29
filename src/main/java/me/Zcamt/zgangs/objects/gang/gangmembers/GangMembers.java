package me.Zcamt.zgangs.objects.gang.gangmembers;

import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangRank;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GangMembers {
    private Gang gang;
    private int maxMembers;
    private final List<UUID> memberList;
    private final List<UUID> playerInvites;

    public GangMembers(int maxMembers, List<UUID> memberList, List<UUID> playerInvites) {
        this.maxMembers = maxMembers;
        this.memberList = memberList;
        this.playerInvites = playerInvites;
    }

    public boolean addPlayerToInvites(GangPlayer gangPlayer) {
        if (isMember(gangPlayer.getUUID())) return false;
        if (playerInvites.contains(gangPlayer.getUUID())) return false;
        playerInvites.add(gangPlayer.getUUID());
        if(gangPlayer.addGangInvite(gang)) {
            gang.serialize();
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayerFromInvites(GangPlayer gangPlayer) {
        if (!playerInvites.contains(gangPlayer.getUUID())) return false;

        playerInvites.remove(gangPlayer.getUUID());
        gangPlayer.removeGangInvite(gang);
        gang.serialize();
        return true;
    }

    public boolean addGangPlayerToGang(GangPlayer gangPlayer) {
        if(memberList.size() >= maxMembers) return false;

        removePlayerFromInvites(gangPlayer);
        if (memberList.contains(gangPlayer.getUUID())) return false;
        if (gangPlayer.getGangUUID() != null) return false;
        memberList.add(gangPlayer.getUUID());
        gangPlayer.setGangID(gang.getUUID());
        gangPlayer.setGangRank(GangRank.RECRUIT);
        gang.serialize();
        return true;
    }

    public boolean removeGangPlayerFromGang(GangPlayer gangPlayer) {
        gangPlayer.setGangRank(GangRank.RECRUIT);
        gangPlayer.setGangID(null);
        memberList.remove(gangPlayer.getUUID());
        removePlayerFromInvites(gangPlayer);
        gang.serialize();
        return true;
    }


    public List<UUID> getMemberList() {
        return Collections.unmodifiableList(memberList);
    }

    public List<UUID> getPlayerInvites() {
        return Collections.unmodifiableList(playerInvites);
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
        gang.serialize();
    }

    public boolean isMember(UUID uuid) {
        return memberList.contains(uuid);
    }

    public int getMemberCount() {
        return memberList.size();
    }

    public boolean isInvited(UUID uuid) {
        return playerInvites.contains(uuid);
    }

    public int getInvitedCount() {
        return playerInvites.size();
    }

    public void setGang(Gang gang) {
        if(gang != null) {
            this.gang = gang;
        }
    }
}
