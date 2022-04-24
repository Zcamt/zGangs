package me.Zcamt.zgangs.objects.gang;


import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Gang {

    //Todo: Potentially add upgradeable gang+ally damage aswell.
    //Todo: Gang shop perhaps aswell
    private final UUID uuid;
    private final long creationDateMillis;
    private String name;
    //Todo: Add MOTD
    private int level;
    private int kills;
    private int deaths;
    private int bank;
    private int maxMembers;
    private int maxAllies;
    private UUID ownerUUID;
    private final List<UUID> memberList;
    private final List<UUID> playerInvites;
    private final List<UUID> alliedGangs;
    private final List<UUID> alliedGangInvitesIncoming;
    private final List<UUID> alliedGangInvitesOutgoing;
    private final List<UUID> rivalGangs;
    private final List<UUID> rivalGangsAgainst;
    private final GangPermissions gangPermissions;

    public Gang(UUID uuid, long creationDateMillis, String name, int level, int kills, int deaths, int bank, int maxMembers, int maxAllies, UUID ownerUUID, List<UUID> memberList, List<UUID> playerInvites, List<UUID> alliedGangs, List<UUID> alliedGangInvitesIncoming, List<UUID> alliedGangInvitesOutgoing, List<UUID> rivalGangs, List<UUID> rivalGangsAgainst, GangPermissions gangPermissions) {
        this.uuid = uuid;
        this.creationDateMillis = creationDateMillis;
        this.name = name;
        this.level = level;
        this.kills = kills;
        this.deaths = deaths;
        this.bank = bank;
        this.maxMembers = maxMembers;
        this.maxAllies = maxAllies;
        this.ownerUUID = ownerUUID;
        this.memberList = memberList;
        this.playerInvites = playerInvites;
        this.alliedGangs = alliedGangs;
        this.alliedGangInvitesIncoming = alliedGangInvitesIncoming;
        this.alliedGangInvitesOutgoing = alliedGangInvitesOutgoing;
        this.rivalGangs = rivalGangs;
        this.rivalGangsAgainst = rivalGangsAgainst;
        this.gangPermissions = gangPermissions;
    }

    public void setName(String name) {
        //Todo: Name checked before this is called
        this.name = name;
        serialize();
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
        if (bank < 0) {
            bank = 0;
        }
        this.bank = bank;
        serialize();
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
        serialize();
    }

    public void setMaxAllies(int maxAllies) {
        this.maxAllies = maxAllies;
        serialize();
    }

    public boolean setOwner(GangPlayer newOwner) {
        if(!this.memberList.contains(newOwner.getUUID())) {
            return false;
        }
        if(this.bank >= Config.transferGangCost) {
            GangPlayer oldOwner = ZGangs.getGangPlayerManager().findById(this.ownerUUID);
            oldOwner.setGangRank(GangRank.CO_OWNER);
            newOwner.setGangRank(GangRank.OWNER);
            this.ownerUUID = newOwner.getUUID();
            setBank(this.bank - Config.transferGangCost);
            serialize();
            return true;
        } else {
            return false;
        }
    }

    public boolean addPlayerToInvites(GangPlayer gangPlayer) {
        if (isMember(gangPlayer.getUUID())) return false;
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
        if(memberList.size() >= maxMembers) return false;

        removePlayerFromInvites(gangPlayer);
        if (memberList.contains(gangPlayer.getUUID())) return false;
        if (gangPlayer.getGangUUID() != null) return false;
        memberList.add(uuid);
        gangPlayer.setGangID(this.uuid);
        gangPlayer.setGangRank(GangRank.RECRUIT);
        serialize();
        return true;
    }

    //Todo: Make removeGangPlayerFromGang method

    public boolean addAlly(Gang gang) {
        if(alliedGangs.size() >= maxAllies) return false;
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

    public long getCreationDateMillis() {
        return creationDateMillis;
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

    public boolean isMember(UUID uuid) {
        return getMemberList().contains(uuid);
    }

    public List<UUID> getMemberList() {
        return Collections.unmodifiableList(memberList);
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
