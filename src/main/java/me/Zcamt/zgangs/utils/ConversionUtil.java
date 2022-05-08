package me.Zcamt.zgangs.utils;

import me.Zcamt.zgangs.objects.gangitems.GangDeliveryItem;
import me.Zcamt.zgangs.objects.gangitems.GangItemDelivery;
import me.Zcamt.zgangs.objects.gangpermissions.GangPermission;
import me.Zcamt.zgangs.objects.gangpermissions.GangPermissions;
import me.Zcamt.zgangs.objects.gang.GangRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConversionUtil {


    public static String uuidListToString(List<UUID> list){
        if(list.size() == 0) return "";
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
        if(string.length() == 0) return uuidList;
        String[] uuidStringArray = string.split(";");
        for(String uuidString : uuidStringArray){
            uuidList.add(UUID.fromString(uuidString));
        }
        return uuidList;
    }


    //Todo: Alle herunder skal testes
    public static String gangMemberMapToString(HashMap<UUID, Integer> memberMap){
        if(memberMap.size() == 0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        Object[] memberMapKeyArray = memberMap.keySet().toArray();
        for(int i = 0; i<memberMapKeyArray.length; i++){
            UUID memberUUID = (UUID) memberMapKeyArray[i];
            stringBuilder.append(memberUUID).append(":").append(memberMap.get(memberUUID));
            if(i+1 != memberMapKeyArray.length) stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public static HashMap<UUID, Integer> stringToGangMemberMap(String members){
        HashMap<UUID, Integer> memberMap = new HashMap<>();
        if(members.length() == 0) return memberMap;
        String[] membersArray = members.split(";");
        for(String memberString : membersArray){
            String[] memberArray = memberString.split(":");
            UUID memberUUID = UUID.fromString(memberArray[0]);
            int memberGangRank = Integer.parseInt(memberArray[1]);
            memberMap.put(memberUUID, memberGangRank);
        }
        return memberMap;
    }


    public static String gangPermissionsToString(HashMap<GangPermission, GangRank> permissionsMap){
        if(permissionsMap.size() == 0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        Object[] permissionMapKeyArray = permissionsMap.keySet().toArray();
        for(int i = 0; i<permissionMapKeyArray.length; i++){
            GangPermission permission = (GangPermission) permissionMapKeyArray[i];
            GangRank rank = permissionsMap.get(permission);
            stringBuilder.append(permission.toString()).append(":").append(rank.getID());
            if(i+1 != permissionMapKeyArray.length) stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public static GangPermissions gangPermissionsFromString(String gangPermissions){
        HashMap<GangPermission, GangRank> permissionMap = new HashMap<>();
        if(gangPermissions.length() == 0) return new GangPermissions(permissionMap);
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


    public static String gangItemDeliveryToString(HashMap<GangDeliveryItem, Integer> deliveredItems){
        if(deliveredItems.size() == 0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        Object[] itemsDeliveredKeyArray = deliveredItems.keySet().toArray();
        for(int i = 0; i<itemsDeliveredKeyArray.length; i++){
            GangDeliveryItem deliveryItem = (GangDeliveryItem) itemsDeliveredKeyArray[i];
            stringBuilder.append(deliveryItem.toString()).append(":").append(deliveredItems.get(deliveryItem));
            if(i+1 != itemsDeliveredKeyArray.length) stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }


    public static GangItemDelivery gangItemDeliveryFromString(String deliveredItems){
        HashMap<GangDeliveryItem, Integer> deliveredItemsMap = new HashMap<>();
        if(deliveredItems.length() == 0) return new GangItemDelivery(deliveredItemsMap);
        String[] itemsDeliveredArray = deliveredItems.split(";");
        for(String itemDeliveryString : itemsDeliveredArray){
            String[] itemArray = itemDeliveryString.split(":");
            String item = itemArray[0].toUpperCase();
            int amount = Integer.parseInt(itemArray[1]);
            deliveredItemsMap.put(GangDeliveryItem.valueOf(item), amount);
        }
        return new GangItemDelivery(deliveredItemsMap);
    }

}
