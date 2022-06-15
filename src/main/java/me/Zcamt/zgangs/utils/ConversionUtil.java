package me.Zcamt.zgangs.utils;

import java.util.*;

public class ConversionUtil {


    public static String uuidListToString(List<UUID> list){
        if(list.size() == 0) return "";
        //list.removeIf(uuid -> uuid == null);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            UUID uuid = list.get(i);
            stringBuilder.append(uuid.toString());
            if(i+1 < list.size()){
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

}
