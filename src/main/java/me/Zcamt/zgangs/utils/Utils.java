package me.Zcamt.zgangs.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Utils {

    public static void sendMessage(Player player, String message){
        player.sendMessage(CC(message));
    }

    public static void sendMessage(Player player, Messages message){
        player.sendMessage(CC(message.getMessage()));
    }

    public static void sendCenteredMessage(Player player, String message){
        final int CENTER_PX = 154;
        if(message == null || message.equals("")) sendMessage(player,"");
        message = CC(message);

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

    public static String CC(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

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

}
