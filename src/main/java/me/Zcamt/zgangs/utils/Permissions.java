package me.Zcamt.zgangs.utils;

public enum Permissions {
    PLAYER("zGangs.player"),
    ADMIN("zGangs.admin"),
    OVERRIDE("zStaffCore.*");

    private String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
