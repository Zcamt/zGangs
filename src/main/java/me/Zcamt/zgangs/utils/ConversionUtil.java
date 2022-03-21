package me.Zcamt.zgangs.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConversionUtil {

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
    public static String gangRankPermissionMapToString(HashMap<String, Integer> permissionsMap){
        StringBuilder stringBuilder = new StringBuilder();
        Object[] permissionMapKeyArray = permissionsMap.keySet().toArray();
        for(int i = 0; i<permissionMapKeyArray.length; i++){
            String permission = (String) permissionMapKeyArray[i];
            stringBuilder.append(permissionMapKeyArray[i]).append(":").append(permissionsMap.get(permission));
            if(i+1 != permissionMapKeyArray.length) stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public static HashMap<String, Integer> stringToGangRankPermissionMap(String gangPermissions){
        HashMap<String, Integer> permissionMap = new HashMap<>();
        String[] permissionsArray = gangPermissions.split(";");
        for(String permissionString : permissionsArray){
            String[] permissionArray = permissionString.split(":");
            String permission = permissionArray[0];
            int requiredRank = Integer.parseInt(permissionArray[1]);
            permissionMap.put(permission, requiredRank);
        }
        return permissionMap;
    }

}
