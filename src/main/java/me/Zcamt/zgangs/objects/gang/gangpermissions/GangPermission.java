package me.Zcamt.zgangs.objects.gang.gangpermissions;

import me.Zcamt.zgangs.objects.gang.GangRank;

public enum GangPermission {
    INVITE_PLAYERS(GangRank.MEMBER),
    MANAGE_INVITES(GangRank.CAPTAIN),
    KICK_MEMBERS(GangRank.CAPTAIN),
    USE_GANGSHOP(GangRank.CAPTAIN),
    GANG_RANKUP(GangRank.CAPTAIN),
    MANAGE_MOTD(GangRank.CO_OWNER),
    MANAGE_ALLIES(GangRank.CAPTAIN),
    MANAGE_RIVALS(GangRank.CAPTAIN),
    MANAGE_MEMBER_RANKS(GangRank.CO_OWNER),
    RENAME_GANG(GangRank.CO_OWNER);

    private final GangRank defaultRequiredRank;

    GangPermission(GangRank defaultRequiredRank) {
        this.defaultRequiredRank = defaultRequiredRank;
    }

    public GangRank getDefaultRequiredRank() {
        return defaultRequiredRank;
    }
}
