package me.Zcamt.zgangs.objects;

import me.Zcamt.zgangs.utils.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Gang {

    private final int id;
    private String name;
    private int level;
    private int kills;
    private int deaths;
    private int bank;
    private UUID ownerUUID;
    private final HashMap<UUID, Integer> memberList;
    private final List<String> playerInvites;
    //Todo: Add invite logic, could also be temp invites stored in cache for X min also add rivals and allies + ally invites perhaps
    
    public Gang(int id, String name, int level, int kills, int deaths, int bank, UUID ownerUUID, HashMap<UUID, Integer> memberList, List<String> playerInvites) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.kills = kills;
        this.deaths = deaths;
        this.bank = bank;
        this.ownerUUID = ownerUUID;
        this.memberList = memberList;
        this.playerInvites = playerInvites;
    }

    public int getId() {
        return id;
    }

    /*public void setId(int id) {
        this.id = id;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public HashMap<UUID, Integer> getMemberList() {
        return memberList;
    }

    public String getSerializedMemberList(){
        return Utilities.serializeGangMemberMap(memberList);
    }

    /*public void setMemberList(HashMap<UUID, Integer> memberList) {
        this.memberList = memberList;
    }*/

    public String getSerializedPlayerInvites(){
        return Utilities.serializeStringListToString(playerInvites);
    }

    public void addGangPlayerToGang(GangPlayer gangPlayer){
        gangPlayer.removeGangInvite(this.id);
        if(memberList.containsKey(gangPlayer.getPlayer().getUniqueId())) return;
        memberList.put(gangPlayer.getPlayer().getUniqueId(), 1);
        gangPlayer.setGangID(this.id);
        gangPlayer.setGangRank(1);
    }

}
