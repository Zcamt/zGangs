package me.Zcamt.zgangs.objects.gang;

public enum GangPermission {
    INVITE_PLAYERS(2),
    MANAGE_INVITES(3),
    KICK_MEMBERS(3),
    USE_GANGSHOP(3),
    GANG_RANKUP(3),
    MANAGE_MOTD(4),
    MANAGE_ALLIES(3),
    MANAGE_RIVALS(3),
    MANAGE_MEMBER_RANKS(4),
    RENAME_GANG(4);

    private int defaultValue;

    GangPermission(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getDefaultValue() {
        return defaultValue;
    }
}
