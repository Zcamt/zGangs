package me.Zcamt.zgangs.utils;

import me.Zcamt.zgangs.managers.ConfigManager;
import me.Zcamt.zgangs.managers.Database;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Utilities {

    private Utilities(){

    }

    public static void sendMessage(Player player, String message){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendMessage(Player player, Messages message){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
    }

    public static void sendCenteredMessage(Player player, String message){
        final int CENTER_PX = 154;
        if(message == null || message.equals("")) sendMessage(player,"");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{

                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        sendMessage(player, sb.toString() + message);
    }

    //Todo: Could/should be moved somewhere else
    public static boolean isGangNameValid(String name, Database database) {
        if(name.length() > 32) return false;
        if(ConfigManager.bannedGangNames.contains(name.toLowerCase())) return false;
        boolean nameExists;
        try {
            nameExists = database.getGangRepository().gangNameExists(name).get();
        } catch (InterruptedException | ExecutionException e) {
            nameExists = true;
        }
        if(nameExists) return false;

        //Todo: Maybe regex support?
        return true;
    }

    public static String serializeGangMemberMap(HashMap<UUID, Integer> memberMap){
        StringBuilder stringBuilder = new StringBuilder();
        Object[] memberMapKeyArray = memberMap.keySet().toArray();
        for(int i = 0; i<memberMapKeyArray.length; i++){
            UUID memberUUID = (UUID) memberMapKeyArray[i];
            stringBuilder.append(memberMapKeyArray[i]).append(":").append(memberMap.get(memberUUID));
            if(i+1 != memberMapKeyArray.length) stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public static HashMap<UUID, Integer> deserializeGangMemberList(String members){
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

    //Todo: Handle empty lists in all of these.
    public static String serializeIntListToString(List<Integer> list){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i<list.size(); i++){
            stringBuilder.append(list.get(i));
            if(i+1 != list.size()) stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public static List<Integer> deSerializeStringToIntList(String string){
        List<Integer> newList = new ArrayList<>();
        for(String s : string.split(";")){
            newList.add(Integer.parseInt(s));
        }
        return newList;
    }

    public static String serializeStringListToString(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i<list.size(); i++){
            stringBuilder.append(list.get(i));
            if(i+1 != list.size()) stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public static List<String> deSerializeStringToStringList(String string){
        List<String> newList = new ArrayList<>();
        for(String s : string.split(";")){
            newList.add(s);
        }
        return newList;
    }

}
