package me.Zcamt.zgangs.utils;

import me.Zcamt.zgangs.objects.gang.GangPermission;
import me.Zcamt.zgangs.objects.gang.GangPermissions;
import me.Zcamt.zgangs.objects.gang.GangRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConversionUtil {

    //Todo: Check if empty list causes issues.
    public static String uuidListToString(List<UUID> list){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            UUID uuid = list.get(i);
            stringBuilder.append(uuid.toString());
            if(i+1 != list.size()){
                stringBuilder.append(";");
            }
        }
        return stringBuilder.toString();
    }
    //Todo: Check if empty list causes issues.
    public static List<UUID> uuidListFromString(String string){
        List<UUID> uuidList = new ArrayList<>();
        String[] uuidStringArray = string.split(";");
        for(String uuidString : uuidStringArray){
            uuidList.add(UUID.fromString(uuidString));
        }
        return uuidList;
    }

    public static String gangMemberMapToString(HashMap<UUID, Integer> memberMap){
        StringBuilder stringBuilder = new StringBuilder();
        Object[] memberMapKeyArray = memberMap.keySet().toArray();
        for(int i = 0; i<memberMapKeyArray.length; i++){
            UUID memberUUID = (UUID) memberMapKeyArray[i];
            stringBuilder.append(memberMapKeyArray[i]).append(":").append(memberMap.get(memberUUID));
            if(i+1 != memberMapKeyArray.length) stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public static HashMap<UUID, Integer> stringToGangMemberMap(String members){
        HashMap<UUID, Integer> memberMap = new HashMap<>();
        String[] membersArray = members.split(";");
        for(String memberString : membersArray){
            String[] memberArray = memberString.split(":");
            UUID memberUUID = UUID.fromString(memberArray[0]);
            int memberGangRank = Integer.parseInt(memberArray[1]);
            memberMap.put(memberUUID, memberGangRank);
        }
        return memberMap;
    }


    //Todo: test the two below
    public static String gangPermissionsToString(HashMap<GangPermission, GangRank> permissionsMap){
        StringBuilder stringBuilder = new StringBuilder();
        Object[] permissionMapKeyArray = permissionsMap.keySet().toArray();
        for(int i = 0; i<permissionMapKeyArray.length; i++){
            GangPermission permission = (GangPermission) permissionMapKeyArray[i];
            GangRank rank = permissionsMap.get(permission);
            stringBuilder.append(permissionMapKeyArray[i]).append(":").append(rank.getID());
            if(i+1 != permissionMapKeyArray.length) stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public static GangPermissions stringToGangPermissions(String gangPermissions){
        HashMap<GangPermission, GangRank> permissionMap = new HashMap<>();
        String[] permissionsArray = gangPermissions.split(";");
        for(String permissionString : permissionsArray){
            String[] permissionArray = permissionString.split(":");
            String permission = permissionArray[0].toUpperCase();
            int rankID = Integer.parseInt(permissionArray[1]);
            GangRank requiredRank = GangRank.getRank(rankID);
            permissionMap.put(GangPermission.valueOf(permission), requiredRank);
        }
        return new GangPermissions(permissionMap);
    }

}
