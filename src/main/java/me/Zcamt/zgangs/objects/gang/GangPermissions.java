package me.Zcamt.zgangs.objects.gang;

import java.util.Collections;
import java.util.HashMap;

public class GangPermissions {

    private final HashMap<GangPermission, Integer> permissions = new HashMap<>();

    public GangPermissions(HashMap<GangPermission, Integer> permissionMap) {
        for (GangPermission gangPermission : GangPermission.values()) {
            setPermission(gangPermission, permissionMap.getOrDefault(gangPermission, 1));
        }
    }

    public void setPermission(GangPermission gangPermission, Integer rankRequired) {
        if (rankRequired > 5) {
            rankRequired = 5;
        } else if (rankRequired < 1) {
            rankRequired = 1;
        }
        permissions.put(gangPermission, rankRequired);
    }

    public Integer getRankRequired(GangPermission gangPermission) {
        return permissions.get(gangPermission);
    }

    public HashMap<GangPermission, Integer> getPermissionsMap() {
        return (HashMap<GangPermission, Integer>) Collections.unmodifiableMap(permissions);
    }
}
