package me.Zcamt.zgangs.objects;

import me.Zcamt.zgangs.utils.Utilities;

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
    private final HashMap<UUID, Integer> memberMap;
    private final List<String> playerInvites;
    private final List<Integer> alliedGangs;
    private final List<Integer> alliedGangInvitesIncoming;
    private final List<Integer> alliedGangInvitesOutgoing;
    private final List<Integer> rivalGangs;
    private final List<Integer> rivalGangsAgainst;

    private final GangRepository gangRepository;
    
    public Gang(int id, String name, int level, int kills, int deaths, int bank, UUID ownerUUID, HashMap<UUID, Integer> memberMap, List<String> playerInvites, List<Integer> alliedGangs, List<Integer> alliedGangInvitesIncoming, List<Integer> alliedGangInvitesOutgoing, List<Integer> rivalGangs, List<Integer> rivalGangsAgainst, GangRepository gangRepository) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.kills = kills;
        this.deaths = deaths;
        this.bank = bank;
        this.ownerUUID = ownerUUID;
        this.memberMap = memberMap;
        this.playerInvites = playerInvites;
        this.alliedGangs = alliedGangs;
        this.alliedGangInvitesIncoming = alliedGangInvitesIncoming;
        this.alliedGangInvitesOutgoing = alliedGangInvitesOutgoing;
        this.rivalGangs = rivalGangs;
        this.rivalGangsAgainst = rivalGangsAgainst;
        this.gangRepository = gangRepository;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        gangRepository.updateGangInDB(this);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        gangRepository.updateGangInDB(this);
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
        gangRepository.updateGangInDB(this);
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        gangRepository.updateGangInDB(this);
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
        gangRepository.updateGangInDB(this);
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        gangRepository.updateGangInDB(this);
    }

    public void addMember(UUID uuid, Integer rank){
        memberMap.put(uuid, rank);
    }

    public void removeMember(UUID uuid){
        memberMap.remove(uuid);
    }

    public String getSerializedMemberList(){
        return Utilities.serializeGangMemberMap(memberMap);
    }

    public String getSerializedPlayerInvites(){
        return Utilities.serializeStringListToString(playerInvites);
    }

    public void addPlayerToInvites(GangPlayer gangPlayer){
        if(playerInvites.contains(gangPlayer.getUUID().toString())) return;
        playerInvites.add(gangPlayer.getUUID().toString());
        gangPlayer.addGangInvite(this.id);
        gangRepository.updateGangInDB(this);
    }

    public void removePlayerFromInvites(GangPlayer gangPlayer){
        playerInvites.remove(gangPlayer.getUUID().toString());
        gangPlayer.removeGangInvite(this.id);
        gangRepository.updateGangInDB(this);
    }

    public void addGangPlayerToGang(GangPlayer gangPlayer){
        playerInvites.remove(gangPlayer.getUUID().toString());
        gangPlayer.removeGangInvite(this.id);
        if(memberMap.containsKey(gangPlayer.getUUID())) return;
        memberMap.put(gangPlayer.getUUID(), 1);
        gangPlayer.setGangID(this.id);
        gangPlayer.setGangRank(1);
        gangRepository.updateGangInDB(this);
    }

    public void addAlly(Gang gang){
        if(!alliedGangs.contains(gang.getId())) {
            alliedGangs.add(gang.getId());
            gang.addAlly(this);
            gangRepository.updateGangInDB(this);
        }
    }

    public void removeAlly(Gang gang){
        if(alliedGangs.contains(gang.getId())) {
            alliedGangs.remove(gang.getId());
            gang.removeAlly(this);
            gangRepository.updateGangInDB(this);
        }
    }

    public void addAllyInviteIncoming(Gang gang){
        if(!alliedGangInvitesIncoming.contains(gang.getId())) {
            alliedGangInvitesIncoming.add(gang.getId());
            gang.addAllyInviteOutgoing(this);
            gangRepository.updateGangInDB(this);
        }
    }

    public void removeAllyInviteIncoming(Gang gang){
        if(alliedGangInvitesIncoming.contains(gang.getId())) {
            alliedGangInvitesIncoming.remove(gang.getId());
            gang.removeAllyInviteOutgoing(this);
            gangRepository.updateGangInDB(this);
        }
    }

    public void addAllyInviteOutgoing(Gang gang){
        if(!alliedGangInvitesOutgoing.contains(gang.getId())) {
            alliedGangInvitesOutgoing.add(gang.getId());
            gang.addAllyInviteIncoming(this);
            gangRepository.updateGangInDB(this);
        }
    }

    public void removeAllyInviteOutgoing(Gang gang){
        if(alliedGangInvitesOutgoing.contains(gang.getId())) {
            alliedGangInvitesOutgoing.remove(gang.getId());
            gang.removeAllyInviteIncoming(this);
            gangRepository.updateGangInDB(this);
        }
    }

    public void addRival(Gang gang){
        if(!rivalGangs.contains(gang.getId())){
            rivalGangs.add(gang.getId());
            gang.addRivalAgainst(this);
            gangRepository.updateGangInDB(this);
        }
    }

    public void removeRival(Gang gang){
        if(rivalGangs.contains(gang.getId())){
            rivalGangs.remove(gang.getId());
            gang.removeRivalAgainst(this);
            gangRepository.updateGangInDB(this);
        }
    }

    public void addRivalAgainst(Gang gang){
        if(!rivalGangsAgainst.contains(gang.getId())){
            rivalGangsAgainst.add(gang.getId());
            gangRepository.updateGangInDB(this);
        }
    }

    public void removeRivalAgainst(Gang gang){
        if(rivalGangsAgainst.contains(gang.getId())){
            rivalGangsAgainst.remove(gang.getId());
            gangRepository.updateGangInDB(this);
        }
    }

    public String getSerializedAllyList(){
        return Utilities.serializeIntListToString(alliedGangs);
    }

    public String getSerializedAllyInvitesIncomingList(){
        return Utilities.serializeIntListToString(alliedGangInvitesIncoming);
    }

    public String getSerializedAllyInvitesOutgoingList(){
        return Utilities.serializeIntListToString(alliedGangInvitesOutgoing);
    }

    public String getSerializedRivalList(){
        return Utilities.serializeIntListToString(rivalGangs);
    }

    public String getSerializedRivalsAgainstList(){
        return Utilities.serializeIntListToString(rivalGangsAgainst);
    }
}
