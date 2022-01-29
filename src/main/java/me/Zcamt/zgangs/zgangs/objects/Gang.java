package me.Zcamt.zgangs.zgangs.objects;

import me.Zcamt.zgangs.zgangs.utils.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Gang {

    //Todo: Update gang object in database during set methods

    private final int id;
    private String name;
    private int level;
    private int kills;
    private int deaths;
    private int bank;
    private UUID ownerUUID;
    private final HashMap<UUID, Integer> memberList;
    private final List<String> playerInvites;
    
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

    public String getSerializedPlayerInvites(){
        return Utilities.serializeStringListToString(playerInvites);
    }

    public void addPlayerToInvites(GangPlayer gangPlayer){
        if(playerInvites.contains(gangPlayer.getUUID().toString())) return;
        playerInvites.add(gangPlayer.getUUID().toString());
    }

    public void removePlayerFromInvites(GangPlayer gangPlayer){
        playerInvites.remove(gangPlayer.getUUID().toString());
    }

    public void addGangPlayerToGang(GangPlayer gangPlayer){
        gangPlayer.removeGangInvite(this.id);
        if(memberList.containsKey(gangPlayer.getUUID())) return;
        memberList.put(gangPlayer.getUUID(), 1);
        gangPlayer.setGangID(this.id);
        gangPlayer.setGangRank(1);
    }

}
