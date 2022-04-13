package me.Zcamt.zgangs.objects.gang;


import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Gang {

    //Todo: Potentially add upgradeable gang+ally damage aswell.
    //Todo: Gang shop perhaps aswell
    private final UUID uuid;
    private String name;
    //Todo: Add MOTD
    private int level;
    private int kills;
    private int deaths;
    private int bank;
    private int maxMembers;
    private int maxAllies;
    private UUID ownerUUID;
    private final HashMap<UUID, Integer> memberMap;
    private final List<UUID> playerInvites;
    private final List<UUID> alliedGangs;
    private final List<UUID> alliedGangInvitesIncoming;
    private final List<UUID> alliedGangInvitesOutgoing;
    private final List<UUID> rivalGangs;
    private final List<UUID> rivalGangsAgainst;
    private final GangPermissions gangPermissions;

    public Gang(UUID uuid, String name, int level, int kills, int deaths, int bank, int maxMembers, int maxAllies, UUID ownerUUID, HashMap<UUID, Integer> memberMap, List<UUID> playerInvites, List<UUID> alliedGangs, List<UUID> alliedGangInvitesIncoming, List<UUID> alliedGangInvitesOutgoing, List<UUID> rivalGangs, List<UUID> rivalGangsAgainst, GangPermissions gangPermissions) {
        this.uuid = uuid;
        this.name = name;
        this.level = level;
        this.kills = kills;
        this.deaths = deaths;
        this.bank = bank;
        this.maxMembers = maxMembers;
        this.maxAllies = maxAllies;
        this.ownerUUID = ownerUUID;
        this.memberMap = memberMap;
        this.playerInvites = playerInvites;
        this.alliedGangs = alliedGangs;
        this.alliedGangInvitesIncoming = alliedGangInvitesIncoming;
        this.alliedGangInvitesOutgoing = alliedGangInvitesOutgoing;
        this.rivalGangs = rivalGangs;
        this.rivalGangsAgainst = rivalGangsAgainst;
        this.gangPermissions = gangPermissions;
    }

    public boolean setName(String name) {
        if (true) { //if name is valid
            this.name = name;
            serialize();
            return true;
        } else {
            return false;
        }
    }

    public void setLevel(int level) {
        if (level < 1) {
            level = 1;
        }
        this.level = level;
        serialize();
    }

    public void setKills(int kills) {
        if (kills > 0) {
            kills = 0;
        }
        this.kills = kills;
        serialize();
    }

    public void setDeaths(int deaths) {
        if (deaths > 0) {
            deaths = 0;
        }
        this.deaths = deaths;
        serialize();
    }

    public void setBank(int bank) {
        if (bank > 0) {
            bank = 0;
        }
        this.bank = bank;
        serialize();
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public void setMaxAllies(int maxAllies) {
        this.maxAllies = maxAllies;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        serialize();
    }

    //Todo: Make setOwner method that handles
    // everything from setting new owner to demoting previous owner
    public void setOwner(GangPlayer gangPlayer) {
        setOwnerUUID(gangPlayer.getUUID());
    }

    public boolean addMember(UUID uuid, Integer rank) {
        memberMap.put(uuid, rank);
        return true;
    }

    public void removeMember(UUID uuid) {
        memberMap.remove(uuid);
    }

    public boolean isMember(UUID uuid) {
        return memberMap.containsKey(uuid);
    }

    public boolean addPlayerToInvites(GangPlayer gangPlayer) {
        if (memberMap.containsKey(gangPlayer.getUUID())) return false;
        if (playerInvites.contains(gangPlayer.getUUID())) return false;
        playerInvites.add(gangPlayer.getUUID());
        gangPlayer.addGangInvite(this.uuid);
        serialize();
        return true;
    }

    public boolean removePlayerFromInvites(GangPlayer gangPlayer) {
        if (!playerInvites.contains(gangPlayer.getUUID())) return false;

        playerInvites.remove(gangPlayer.getUUID());
        gangPlayer.removeGangInvite(this);
        serialize();
        return true;
    }

    public boolean addGangPlayerToGang(GangPlayer gangPlayer) {
        //Todo: add check for limit
        //removePlayerFromInvites(gangPlayer);
        playerInvites.remove(gangPlayer.getUUID());
        gangPlayer.removeGangInvite(this);
        if (memberMap.containsKey(gangPlayer.getUUID())) return false;
        if (gangPlayer.getGangUUID() != null) return false;
        addMember(gangPlayer.getUUID(), 1);
        gangPlayer.setGangID(this.uuid);
        gangPlayer.setGangRank(1);
        serialize();
        return true;
    }

    public boolean addAlly(Gang gang) {
        //Todo: add check for limit
        if (alliedGangs.contains(gang.getUUID())) return false;

        alliedGangs.add(gang.getUUID());
        gang.addAlly(this);
        serialize();
        return true;
    }

    public boolean isAllied(UUID gangUUID) {
        return alliedGangs.contains(gangUUID);
    }

    public boolean removeAlly(Gang gang) {
        if (!alliedGangs.contains(gang.getUUID())) return false;

        alliedGangs.remove(gang.getUUID());
        gang.removeAlly(this);
        serialize();
        return true;
    }

    public boolean addAllyInviteIncoming(Gang gang) {
        if (alliedGangInvitesIncoming.contains(gang.getUUID())) return false;

        alliedGangInvitesIncoming.add(gang.getUUID());
        gang.addAllyInviteOutgoing(this);
        serialize();
        return true;
    }

    public boolean allyInviteIncomingContains(UUID gangUUID) {
        return alliedGangInvitesIncoming.contains(gangUUID);
    }

    public boolean removeAllyInviteIncoming(Gang gang) {
        if (!alliedGangInvitesIncoming.contains(gang.getUUID())) return false;

        alliedGangInvitesIncoming.remove(gang.getUUID());
        gang.removeAllyInviteOutgoing(this);
        serialize();
        return true;

    }

    public boolean addAllyInviteOutgoing(Gang gang) {
        if (alliedGangInvitesOutgoing.contains(gang.getUUID())) return false;

        alliedGangInvitesOutgoing.add(gang.getUUID());
        gang.addAllyInviteIncoming(this);
        serialize();
        return true;

    }

    public boolean allyInviteOutgoingContains(UUID gangUUID) {
        return alliedGangInvitesOutgoing.contains(gangUUID);
    }

    public boolean removeAllyInviteOutgoing(Gang gang) {
        if (!alliedGangInvitesOutgoing.contains(gang.getUUID())) return false;

        alliedGangInvitesOutgoing.remove(gang.getUUID());
        gang.removeAllyInviteIncoming(this);
        serialize();
        return true;
    }

    public boolean addRival(Gang gang) {
        if (rivalGangs.contains(gang.getUUID())) return false;

        rivalGangs.add(gang.getUUID());
        gang.addRivalAgainst(this);
        serialize();
        return true;
    }

    public boolean isRival(UUID gangUUID) {
        return rivalGangs.contains(gangUUID);
    }

    public boolean removeRival(Gang gang) {
        if (!rivalGangs.contains(gang.getUUID())) return false;

        rivalGangs.remove(gang.getUUID());
        gang.removeRivalAgainst(this);
        serialize();
        return true;
    }

    public boolean addRivalAgainst(Gang gang) {
        if (rivalGangsAgainst.contains(gang.getUUID())) return false;

        rivalGangsAgainst.add(gang.getUUID());
        serialize();
        return true;
    }

    public boolean isRivalAgainst(UUID gangUUID) {
        return rivalGangsAgainst.contains(gangUUID);
    }

    public boolean removeRivalAgainst(Gang gang) {
        if (!rivalGangsAgainst.contains(gang.getUUID())) return false;

        rivalGangsAgainst.remove(gang.getUUID());
        serialize();
        return true;
    }

    public boolean rankUp() {
        //Todo: Add rankup logic
        return true;
    }

    @NotNull
    public String toJson() {
        return ZGangs.GSON.toJson(this);
    }

    public void serialize() {
        //Todo: Do async
        Document document = Document.parse(toJson());
        ZGangs.getDatabase().getGangCollection()
                .replaceOne(Filters.eq("_id",
                                String.valueOf(this.uuid)),
                        document,
                        new ReplaceOptions().upsert(true));
    }


    public GangPermissions getGangPermissions() {
        return gangPermissions;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBank() {
        return bank;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public int getMaxAllies() {
        return maxAllies;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
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

}
