package me.Zcamt.zgangs.objects.gang;


import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.objects.gang.gangallies.GangAllies;
import me.Zcamt.zgangs.objects.gang.gangitem.GangItemDelivery;
import me.Zcamt.zgangs.objects.gang.ganglevel.GangLevel;
import me.Zcamt.zgangs.objects.gang.gangmembers.GangMembers;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermissions;
import me.Zcamt.zgangs.objects.gang.gangrivals.GangRivals;
import me.Zcamt.zgangs.objects.gang.gangstats.GangStats;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gang.ganglevel.GangLevelManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Gang {
    //Todo: Potentially add upgradeable gang+ally damage aswell.
    //Todo: Add MOTD, could/should be a wrapper like class

    //Could make this into a gang info wrapper
    private final UUID uuid;
    private UUID ownerUUID;
    private final long creationDateMillis;
    private String name;
    private int level; //Todo: Could change to GangLevel object
    private int bank;

    //Todo: Check that all wrapper classes saves/serializes after setting something for the gang
    private final GangStats gangStats;
    private final GangMembers gangMembers;
    private final GangAllies gangAllies;
    private final GangRivals gangRivals;
    private final GangPermissions gangPermissions;

    //Todo: add adapters and "gang" variable to perms and itemDelivery
    private final GangItemDelivery gangItemDelivery;

    public Gang(UUID uuid, UUID ownerUUID, long creationDateMillis, String name, int level, int bank,
                GangStats gangStats,
                GangMembers gangMembers,
                GangAllies gangAllies,
                GangRivals gangRivals,
                GangPermissions gangPermissions,
                GangItemDelivery gangItemDelivery) {
        this.uuid = uuid;
        this.ownerUUID = ownerUUID;
        this.creationDateMillis = creationDateMillis;
        this.name = name;
        this.level = level;
        this.bank = bank;
        this.gangStats = gangStats;
        gangStats.setGang(this);
        this.gangMembers = gangMembers;
        gangMembers.setGang(this);
        this.gangAllies = gangAllies;
        gangAllies.setGang(this);
        this.gangRivals = gangRivals;
        gangRivals.setGang(this);
        this.gangPermissions = gangPermissions;
        gangPermissions.setGang(this);
        this.gangItemDelivery = gangItemDelivery;
        gangItemDelivery.setGang(this);
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

    public void setBank(int bank) {
        if (bank < 0) {
            bank = 0;
        }
        this.bank = bank;
        serialize();
    }

    public boolean setOwner(GangPlayer newOwner) {
        if(!this.getGangMembers().isMember(newOwner.getUUID())) {
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

    public boolean rankUp() {
        GangLevelManager gangLevelManager = ZGangs.getGangLevelManager();
        if(this.level >= gangLevelManager.getLastLevelInt()){
            return false;
        }
        GangLevel gangLevel = gangLevelManager.getGangLevelFromInt(this.level+1);
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
        for (UUID memberUUID : this.getGangMembers().getMemberList()){
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

    public int getBank() {
        return bank;
    }

    public GangStats getGangStats() {
        return gangStats;
    }

    public GangMembers getGangMembers() {
        return gangMembers;
    }

    public GangAllies getGangAllies() {
        return gangAllies;
    }

    public GangRivals getGangRivals() {
        return gangRivals;
    }

    public GangPermissions getGangPermissions() {
        return gangPermissions;
    }

    public GangItemDelivery getGangItemDelivery() {
        return gangItemDelivery;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gang gang = (Gang) o;
        return uuid.equals(gang.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
