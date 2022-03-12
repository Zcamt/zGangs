package me.Zcamt.zgangs.objects;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.Zcamt.zgangs.ZGangs;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GangPlayer {

    //Todo: Might wanna change to only use UUIDs as that allows for creation of GangPlayer object without player being online.

    private UUID uuid;
    private UUID gangUUID;
    private int gangRank;
    private List<UUID> gangInvites;

    public GangPlayer(UUID uuid, UUID gangUUID, int gangRank, List<UUID> gangInvites) {
        this.uuid = uuid;
        this.gangUUID = gangUUID;
        this.gangRank = gangRank;
        this.gangInvites = gangInvites;
    }

    public UUID getUUID() {
        return uuid;
    }
    public OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(uuid);
    }

    public UUID getGangUUID() {
        return gangUUID;
    }

    public void setGangID(UUID gangUUID) {
        this.gangUUID = gangUUID;
        serialize();
    }

    public int getGangRank() {
        return gangRank;
    }
    public void setGangRank(int gangRank) {
        this.gangRank = gangRank;
        serialize();
    }

    public List<UUID> getGangInvites() {
        return Collections.unmodifiableList(gangInvites);
    }

    public void addGangInvite(UUID gangUUID) {
        gangInvites.add(gangUUID);
        serialize();
    }
    public void removeGangInvite(Gang gang) {
        if(gangInvites.contains(gang.getUUID())) {
            gangInvites.remove(gang.getUUID());
            gang.removePlayerFromInvites(this);
        }
        serialize();
        gang.serialize();
    }
    public boolean gangInvitesContains(UUID gangUUID) {
        return gangInvites.contains(gangUUID);
    }


    @NotNull
    public String toJson(){
        return ZGangs.GSON.toJson(this);
    }

    public void serialize(){
        //Todo: Do async
        Document document = Document.parse(toJson());
        ZGangs.getDatabase().getGangCollection()
                .replaceOne(Filters.eq("_id",
                        this.uuid.toString()),
                        document,
                        new ReplaceOptions().upsert(true));
    }

}
