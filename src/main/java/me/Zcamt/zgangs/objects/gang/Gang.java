package me.Zcamt.zgangs.objects.gang;


import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.managers.GangLevelManager;
import me.Zcamt.zgangs.objects.gangitems.GangItemDelivery;
import me.Zcamt.zgangs.objects.ganglevels.GangLevel;
import me.Zcamt.zgangs.objects.gangpermissions.GangPermissions;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("jol")
public class Gang {

    //Todo: Potentially add upgradeable gang+ally damage aswell.
    private final UUID uuid;
    private final long creationDateMillis;
    private String name;
    //Todo: Add MOTD
    private int level;
    private int kills;
    private int guardKills;
    private int officerPlusKills;
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
    private final GangItemDelivery gangItemDelivery;

    public Gang(UUID uuid, long creationDateMillis, String name, int level, int kills, int guardKills, int officerPlusKills, int deaths, int bank, int maxMembers, int maxAllies, UUID ownerUUID, List<UUID> memberList, List<UUID> playerInvites, List<UUID> alliedGangs, List<UUID> alliedGangInvitesIncoming, List<UUID> alliedGangInvitesOutgoing, List<UUID> rivalGangs, List<UUID> rivalGangsAgainst, GangPermissions gangPermissions, GangItemDelivery gangItemDelivery) {
        this.uuid = uuid;
        this.creationDateMillis = creationDateMillis;
        this.name = name;
        this.level = level;
        this.kills = kills;
        this.guardKills = guardKills;
        this.officerPlusKills = officerPlusKills;
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
        this.gangItemDelivery = gangItemDelivery;
    }

    public void setName(String name) {
        //name checked before this is called
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

    public void setGuardKills(int guardKills) {
        if (guardKills > 0) {
            guardKills = 0;
        }
        this.guardKills = guardKills;
        serialize();
    }

    public void setOfficerPlusKills(int officerPlusKills) {
        if (officerPlusKills > 0) {
            officerPlusKills = 0;
        }
        this.officerPlusKills = officerPlusKills;
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

    public boolean removeRivalAgainst(Gang gang) {
        if (!rivalGangsAgainst.contains(gang.getUUID())) return false;

        rivalGangsAgainst.remove(gang.getUUID());
        serialize();
        return true;
    }

    public boolean rankUp() {
        GangLevelManager gangLevelManager = ZGangs.getGangLevelManager();
        if(this.level >= gangLevelManager.getLastLevel()){
            return false;
        }
        GangLevel gangLevel = gangLevelManager.getLevelFromInt(this.level+1);
        if(gangLevel.requirementsMet(this)){
            //Todo: Add rankup logic
            this.level = this.level+1;
            setBank(this.bank - gangLevel.getCost());
            return true;
        } else {
            return false;
        }
    }

    public void sendMessageToOnlineMembers(String message){
        for (UUID memberUUID : this.memberList){
            Player memberPlayer = Bukkit.getPlayer(memberUUID);
            if(memberPlayer == null) continue;
            ChatUtil.sendMessage(memberPlayer, message);
        }
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

    public int getGuardKills() {
        return guardKills;
    }

    public int getOfficerPlusKills() {
        return officerPlusKills;
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

    public boolean isAllied(UUID gangUUID) {
        return alliedGangs.contains(gangUUID);
    }

    public boolean isRival(UUID gangUUID) {
        return rivalGangs.contains(gangUUID);
    }

    public boolean isRivalAgainst(UUID gangUUID) {
        return rivalGangsAgainst.contains(gangUUID);
    }

    public boolean allyInviteIncomingContains(UUID gangUUID) {
        return alliedGangInvitesIncoming.contains(gangUUID);
    }

    public boolean allyInviteOutgoingContains(UUID gangUUID) {
        return alliedGangInvitesOutgoing.contains(gangUUID);
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

    public GangPermissions getGangPermissions() {
        return gangPermissions;
    }

    public GangItemDelivery getGangItemDelivery() {
        return gangItemDelivery;
    }

}
