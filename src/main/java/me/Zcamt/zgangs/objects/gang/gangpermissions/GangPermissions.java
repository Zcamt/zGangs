package me.Zcamt.zgangs.objects.gang.gangpermissions;


import me.Zcamt.zgangs.objects.gang.GangRank;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GangPermissions {

    private final Map<GangPermission, GangRank> permissions = new HashMap<>();

    public GangPermissions(Map<GangPermission, GangRank> permissionMap) {
        for (GangPermission gangPermission : GangPermission.values()) {
            setPermission(gangPermission, permissionMap.getOrDefault(gangPermission, gangPermission.getDefaultRequiredRank()));
        }
    }

    public void setPermission(GangPermission gangPermission, GangRank rankRequired) {
        int rankID = rankRequired.getID();

        if (rankID > 5) {
            rankID = 5;
        } else if (rankID < 1) {
            rankID = 1;
        }
        rankRequired = GangRank.getRank(rankID);

        permissions.put(gangPermission, rankRequired);
    }

    public GangRank getRankRequired(GangPermission gangPermission) {
        return permissions.get(gangPermission);
    }

    public Map<GangPermission, GangRank> getPermissionsMap() {
        return Collections.unmodifiableMap(permissions);
    }
}