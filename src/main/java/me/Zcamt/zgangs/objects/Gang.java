package me.Zcamt.zgangs.objects;


import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.Zcamt.zgangs.ZGangs;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Gang {

    private final UUID uuid;
    private String name;
    private int level;
    private int kills;
    private int deaths;
    private int bank;
    private UUID ownerUUID;
    private final HashMap<UUID, Integer> memberMap;
    private final List<UUID> playerInvites;
    private final List<UUID> alliedGangs;
    private final List<UUID> alliedGangInvitesIncoming;
    private final List<UUID> alliedGangInvitesOutgoing;
    private final List<UUID> rivalGangs;
    private final List<UUID> rivalGangsAgainst;
    private final HashMap<String, Integer> rankPermissionMap;
    
    public Gang(UUID uuid, String name, int level, int kills, int deaths, int bank, UUID ownerUUID, HashMap<UUID, Integer> memberMap, List<UUID> playerInvites, List<UUID> alliedGangs, List<UUID> alliedGangInvitesIncoming, List<UUID> alliedGangInvitesOutgoing, List<UUID> rivalGangs, List<UUID> rivalGangsAgainst, HashMap<String, Integer> rankPermissionMap) {
        this.uuid = uuid;
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
        this.rankPermissionMap = rankPermissionMap;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        serialize();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        serialize();
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
        serialize();
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        serialize();
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
        serialize();
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        serialize();
    }

    public HashMap<UUID, Integer> getMemberMap() {
        return (HashMap<UUID, Integer>) Collections.unmodifiableMap(memberMap);
    }

    public List<UUID> getAlliedGangs() {
        return Collections.unmodifiableList(alliedGangs);
    }

    public List<UUID> getRivalGangs() {
        return Collections.unmodifiableList(rivalGangs);
    }

    public List<UUID> getRivalGangsAgainst() {
        return Collections.unmodifiableList(rivalGangsAgainst);
    }

    public List<UUID> getPlayerInvites() {
        return Collections.unmodifiableList(playerInvites);
    }

    public List<UUID> getAlliedGangInvitesIncoming() {
        return Collections.unmodifiableList(alliedGangInvitesIncoming);
    }

    public List<UUID> getAlliedGangInvitesOutgoing() {
        return Collections.unmodifiableList(alliedGangInvitesOutgoing);
    }

    public void addMember(UUID uuid, Integer rank){
        memberMap.put(uuid, rank);
    }

    public void removeMember(UUID uuid){
        memberMap.remove(uuid);
    }

    public boolean isMember(UUID uuid){
        return memberMap.containsKey(uuid);
    }

    public void addPlayerToInvites(GangPlayer gangPlayer){
        if(playerInvites.contains(gangPlayer.getUUID())) return;
        playerInvites.add(gangPlayer.getUUID());
        gangPlayer.addGangInvite(this.uuid);
        serialize();
    }

    public void removePlayerFromInvites(GangPlayer gangPlayer){
        if(playerInvites.contains(gangPlayer.getUUID())) {
            playerInvites.remove(gangPlayer.getUUID());
            gangPlayer.removeGangInvite(this);
        }
        serialize();
    }

    public void addGangPlayerToGang(GangPlayer gangPlayer){
        playerInvites.remove(gangPlayer.getUUID());
        gangPlayer.removeGangInvite(this);
        if(memberMap.containsKey(gangPlayer.getUUID())) return;
        memberMap.put(gangPlayer.getUUID(), 1);
        gangPlayer.setGangID(this.uuid);
        gangPlayer.setGangRank(1);
        serialize();
    }

    public void addAlly(Gang gang){
        if(!alliedGangs.contains(gang.getUUID())) {
            alliedGangs.add(gang.getUUID());
            gang.addAlly(this);
            serialize();
        }
    }

    public boolean isAllied(UUID gangUUID){
        return alliedGangs.contains(gangUUID);
    }

    public void removeAlly(Gang gang){
        if(alliedGangs.contains(gang.getUUID())) {
            alliedGangs.remove(gang.getUUID());
            gang.removeAlly(this);
            serialize();
        }
    }

    public void addAllyInviteIncoming(Gang gang){
        if(!alliedGangInvitesIncoming.contains(gang.getUUID())) {
            alliedGangInvitesIncoming.add(gang.getUUID());
            gang.addAllyInviteOutgoing(this);
            serialize();
        }
    }

    public boolean allyInviteIncomingContains(UUID gangUUID){
        return alliedGangInvitesIncoming.contains(gangUUID);
    }

    public void removeAllyInviteIncoming(Gang gang){
        if(alliedGangInvitesIncoming.contains(gang.getUUID())) {
            alliedGangInvitesIncoming.remove(gang.getUUID());
            gang.removeAllyInviteOutgoing(this);
            serialize();
        }
    }

    public void addAllyInviteOutgoing(Gang gang){
        if(!alliedGangInvitesOutgoing.contains(gang.getUUID())) {
            alliedGangInvitesOutgoing.add(gang.getUUID());
            gang.addAllyInviteIncoming(this);
            serialize();
        }
    }

    public boolean allyInviteOutgoingContains(UUID gangUUID){
        return alliedGangInvitesOutgoing.contains(gangUUID);
    }

    public void removeAllyInviteOutgoing(Gang gang){
        if(alliedGangInvitesOutgoing.contains(gang.getUUID())) {
            alliedGangInvitesOutgoing.remove(gang.getUUID());
            gang.removeAllyInviteIncoming(this);
            serialize();
        }
    }

    public void addRival(Gang gang){
        if(!rivalGangs.contains(gang.getUUID())){
            rivalGangs.add(gang.getUUID());
            gang.addRivalAgainst(this);
            serialize();
        }
    }

    public boolean isRival(UUID gangUUID){
        return rivalGangs.contains(gangUUID);
    }

    public void removeRival(Gang gang){
        if(rivalGangs.contains(gang.getUUID())){
            rivalGangs.remove(gang.getUUID());
            gang.removeRivalAgainst(this);
            serialize();
        }
    }

    public void addRivalAgainst(Gang gang){
        if(!rivalGangsAgainst.contains(gang.getUUID())){
            rivalGangsAgainst.add(gang.getUUID());
            serialize();
        }
    }

    public boolean isRivalAgainst(UUID gangUUID){
        return rivalGangsAgainst.contains(gangUUID);
    }

    public void removeRivalAgainst(Gang gang){
        if(rivalGangsAgainst.contains(gang.getUUID())){
            rivalGangsAgainst.remove(gang.getUUID());
            serialize();
        }
    }

    public HashMap<String, Integer> getRankPermissionMap() {
        return (HashMap<String, Integer>) Collections.unmodifiableMap(rankPermissionMap);
    }

    //Todo: add logic to interact with permission map


    @NotNull
    public String toJson(){
        return ZGangs.GSON.toJson(this);
    }

    public void serialize(){
        //Todo: Do async
        Document document = Document.parse(toJson());
        ZGangs.getDatabase().getGangCollection()
                .replaceOne(Filters.eq("_id",
                        String.valueOf(this.uuid)),
                        document,
                        new ReplaceOptions().upsert(true));
    }

}
