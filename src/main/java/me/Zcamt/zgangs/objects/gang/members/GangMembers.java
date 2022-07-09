package me.Zcamt.zgangs.objects.gang.members;

import com.google.common.collect.ImmutableList;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;

import java.util.List;
import java.util.UUID;

public class GangMembers {
    private Gang gang;
    private int maxMembers;
    private int memberDamagePercent;
    private final List<UUID> memberList;
    private final List<UUID> playerInvites;

    public GangMembers(int maxMembers, int memberDamagePercent, List<UUID> memberList, List<UUID> playerInvites) {
        this.maxMembers = maxMembers;
        this.memberDamagePercent = memberDamagePercent;
        this.memberList = memberList;
        this.playerInvites = playerInvites;
    }

    public boolean addPlayerToInvites(GangPlayer gangPlayer) {
        if (isMember(gangPlayer.getUUID())) return false;
        if (playerInvites.contains(gangPlayer.getUUID())) return false;
        playerInvites.add(gangPlayer.getUUID());
        if(gangPlayer.addGangInvite(gang)) {
            gang.sendMessageToOnlineMembers(Messages.inviteSentToPlayer(gangPlayer.getOfflinePlayer().getName()));
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

        gang.sendMessageToOnlineMembers(Messages.playerJoinedGang(gangPlayer.getOfflinePlayer().getName()));
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

    public void setMaxMembers(int maxMembers) {
        if(maxMembers < 1) {
            maxMembers = 1;
        }
        this.maxMembers = maxMembers;
        gang.serialize();
    }

    public void setMemberDamagePercent(int memberDamagePercent) {
        if(memberDamagePercent > 100) {
            memberDamagePercent = 100;
        } else if(memberDamagePercent < 0) {
            memberDamagePercent = 0;
        }
        this.memberDamagePercent = memberDamagePercent;
        gang.serialize();
    }


    public List<UUID> getMemberList() {
        return ImmutableList.copyOf(memberList);
    }

    public List<UUID> getPlayerInvites() {
        return ImmutableList.copyOf(playerInvites);
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public int getMemberDamagePercent() {
        return memberDamagePercent;
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
        if(this.gang == null && gang != null) {
            this.gang = gang;
        }
    }
}
