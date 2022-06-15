package me.Zcamt.zgangs.objects.gang.permissions;


import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangRank;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GangPermissions {

    private Gang gang;
    private GangRank minRankToInvitePlayers;
    private GangRank minRankToManageInvites;
    private GangRank minRankToKickMembers;
    private GangRank minRankToUseShop;
    private GangRank minRankToRankUp;
    private GangRank minRankToManageMOTD;
    private GangRank minRankToManageAllies;
    private GangRank minRankToManageRivals;
    private GangRank minRankToManageMemberRanks;
    private GangRank minRankToRenameGang;

    public GangPermissions(GangRank minRankToInvitePlayers,
                           GangRank minRankToManageInvites,
                           GangRank minRankToKickMembers,
                           GangRank minRankToUseShop,
                           GangRank minRankToRankUp,
                           GangRank minRankToManageMOTD,
                           GangRank minRankToManageAllies,
                           GangRank minRankToManageRivals,
                           GangRank minRankToManageMemberRanks,
                           GangRank minRankToRenameGang) {
        this.minRankToInvitePlayers = minRankToInvitePlayers;
        this.minRankToManageInvites = minRankToManageInvites;
        this.minRankToKickMembers = minRankToKickMembers;
        this.minRankToUseShop = minRankToUseShop;
        this.minRankToRankUp = minRankToRankUp;
        this.minRankToManageMOTD = minRankToManageMOTD;
        this.minRankToManageAllies = minRankToManageAllies;
        this.minRankToManageRivals = minRankToManageRivals;
        this.minRankToManageMemberRanks = minRankToManageMemberRanks;
        this.minRankToRenameGang = minRankToRenameGang;
    }


    public void setMinRankToInvitePlayers(GangRank minRankToInvitePlayers) {
        this.minRankToInvitePlayers = minRankToInvitePlayers;
        gang.serialize();
    }

    public void setMinRankToManageInvites(GangRank minRankToManageInvites) {
        this.minRankToManageInvites = minRankToManageInvites;
        gang.serialize();
    }

    public void setMinRankToKickMembers(GangRank minRankToKickMembers) {
        this.minRankToKickMembers = minRankToKickMembers;
        gang.serialize();
    }

    public void setMinRankToUseShop(GangRank minRankToUseShop) {
        this.minRankToUseShop = minRankToUseShop;
        gang.serialize();
    }

    public void setMinRankToRankUp(GangRank minRankToRankUp) {
        this.minRankToRankUp = minRankToRankUp;
        gang.serialize();
    }

    public void setMinRankToManageMOTD(GangRank minRankToManageMOTD) {
        this.minRankToManageMOTD = minRankToManageMOTD;
        gang.serialize();
    }

    public void setMinRankToManageAllies(GangRank minRankToManageAllies) {
        this.minRankToManageAllies = minRankToManageAllies;
        gang.serialize();
    }

    public void setMinRankToManageRivals(GangRank minRankToManageRivals) {
        this.minRankToManageRivals = minRankToManageRivals;
        gang.serialize();
    }

    public void setMinRankToManageMemberRanks(GangRank minRankToManageMemberRanks) {
        this.minRankToManageMemberRanks = minRankToManageMemberRanks;
        gang.serialize();
    }

    public void setMinRankToRenameGang(GangRank minRankToRenameGang) {
        this.minRankToRenameGang = minRankToRenameGang;
        gang.serialize();
    }


    public GangRank getMinRankToInvitePlayers() {
        return minRankToInvitePlayers;
    }

    public GangRank getMinRankToManageInvites() {
        return minRankToManageInvites;
    }

    public GangRank getMinRankToKickMembers() {
        return minRankToKickMembers;
    }

    public GangRank getMinRankToUseShop() {
        return minRankToUseShop;
    }

    public GangRank getMinRankToRankUp() {
        return minRankToRankUp;
    }

    public GangRank getMinRankToManageMOTD() {
        return minRankToManageMOTD;
    }

    public GangRank getMinRankToManageAllies() {
        return minRankToManageAllies;
    }

    public GangRank getMinRankToManageRivals() {
        return minRankToManageRivals;
    }

    public GangRank getMinRankToManageMemberRanks() {
        return minRankToManageMemberRanks;
    }

    public GangRank getMinRankToRenameGang() {
        return minRankToRenameGang;
    }

    public void setGang(Gang gang) {
        if(this.gang == null && gang != null) {
            this.gang = gang;
        }
    }
}
