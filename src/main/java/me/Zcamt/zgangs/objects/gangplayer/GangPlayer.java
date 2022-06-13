package me.Zcamt.zgangs.objects.gangplayer;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangRank;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GangPlayer {

    private UUID uuid;
    private @Nullable UUID gangUUID;
    private GangRank gangRank;
    private List<UUID> gangInvites;
    //Todo: GangPlayerSettings

    public GangPlayer(UUID uuid, @Nullable UUID gangUUID, GangRank gangRank, List<UUID> gangInvites) {
        this.uuid = uuid;
        this.gangUUID = gangUUID;
        this.gangRank = gangRank;
        this.gangInvites = gangInvites;
    }

    public void setGangID(UUID gangUUID) {
        this.gangUUID = gangUUID;
        serialize();
    }

    public void setGangRank(GangRank gangRank) {
        this.gangRank = gangRank;
        serialize();
    }

    public boolean addGangInvite(Gang gang) {
        if(gangInvites.contains(gang.getUUID())) return false;

        gangInvites.add(gang.getUUID());
        gang.getGangMembers().addPlayerToInvites(this);
        serialize();
        return true;
    }

    public boolean removeGangInvite(Gang gang) {
        if(!gangInvites.contains(gang.getUUID())) return false;

        gangInvites.remove(gang.getUUID());
        gang.getGangMembers().removePlayerFromInvites(this);
        serialize();
        return true;
    }


    @NotNull
    public String toJson(){
        return ZGangs.GSON.toJson(this);
    }

    public void serialize(){
        //Todo: Do async
        Document document = Document.parse(toJson());
        ZGangs.getDatabase().getGangPlayerCollection()
                .replaceOne(Filters.eq("_id",
                        this.uuid.toString()),
                        document,
                        new ReplaceOptions().upsert(true));
    }


    public UUID getUUID() {
        return uuid;
    }

    public OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(uuid);
    }

    @Nullable
    public UUID getGangUUID() {
        return gangUUID;
    }

    public GangRank getGangRank() {
        return gangRank;
    }

    public List<UUID> getGangInvites() {
        return Collections.unmodifiableList(gangInvites);
    }

    public boolean gangInvitesContains(UUID gangUUID) {
        return gangInvites.contains(gangUUID);
    }

    public boolean isInGang(){
        return this.gangUUID != null;
    }

}
