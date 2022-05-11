package me.Zcamt.zgangs.objects.gang;


import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.objects.gang.gangallies.GangAllies;
import me.Zcamt.zgangs.objects.gang.ganglevel.GangLevelManager;
import me.Zcamt.zgangs.objects.gang.gangitem.GangItemDelivery;
import me.Zcamt.zgangs.objects.gang.ganglevel.GangLevel;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermissions;
import me.Zcamt.zgangs.objects.gang.gangrivals.GangRivals;
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
    private UUID ownerUUID;
    private final long creationDateMillis;
    private String name;
    private int level;
    private int bank;

    //Might be worth changing some of these into a stats class.
    private int kills;
    private int guardKills;
    private int officerPlusKills;
    private int deaths;

    //Might be worth changing all these to wrapper like classes and handle all member, ally & rival stuff in their own classes.
    private int maxMembers;
    private final List<UUID> memberList;
    private final List<UUID> playerInvites;

    private final GangAllies gangAllies;
    private final GangRivals gangRivals;

    //Todo: Add MOTD, could/should be a wrapper like class
    private final GangPermissions gangPermissions;
    private final GangItemDelivery gangItemDelivery;

    public Gang(UUID uuid, UUID ownerUUID, long creationDateMillis, String name, int level, int bank,
                int kills, int guardKills, int officerPlusKills, int deaths,
                int maxMembers, List<UUID> memberList, List<UUID> playerInvites,
                GangAllies gangAllies,
                GangRivals gangRivals,
                GangPermissions gangPermissions,
                GangItemDelivery gangItemDelivery) {
        this.uuid = uuid;
        this.ownerUUID = ownerUUID;
        this.creationDateMillis = creationDateMillis;
        this.name = name;
        this.level = level;
        this.kills = kills;
        this.guardKills = guardKills;
        this.officerPlusKills = officerPlusKills;
        this.deaths = deaths;
        this.bank = bank;
        this.maxMembers = maxMembers;
        this.memberList = memberList;
        this.playerInvites = playerInvites;
        this.gangAllies = gangAllies;
        gangAllies.setGang(this);
        this.gangRivals = gangRivals;
        gangRivals.setGang(this);
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
        this.gangAllies.setMaxAllies(maxAllies);
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

    public UUID getOwnerUUID() {
        return ownerUUID;
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

    public boolean isMember(UUID uuid) {
        return getMemberList().contains(uuid);
    }

    public GangAllies getGangAllies() {
        return gangAllies;
    }

    public GangRivals getGangRivals() {
        return gangRivals;
    }

    public List<UUID> getMemberList() {
        return Collections.unmodifiableList(memberList);
    }

    public List<UUID> getPlayerInvites() {
        return Collections.unmodifiableList(playerInvites);
    }

    public GangPermissions getGangPermissions() {
        return gangPermissions;
    }

    public GangItemDelivery getGangItemDelivery() {
        return gangItemDelivery;
    }

}
