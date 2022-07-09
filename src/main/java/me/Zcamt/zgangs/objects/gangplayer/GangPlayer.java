package me.Zcamt.zgangs.objects.gangplayer;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.objects.gangplayer.settings.GangPlayerSettings;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
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
    private final GangPlayerSettings gangPlayerSettings;

    public GangPlayer(UUID uuid, @Nullable UUID gangUUID, GangRank gangRank, List<UUID> gangInvites, GangPlayerSettings gangPlayerSettings) {
        this.uuid = uuid;
        this.gangUUID = gangUUID;
        this.gangRank = gangRank;
        this.gangInvites = gangInvites;
        this.gangPlayerSettings = gangPlayerSettings;
        gangPlayerSettings.setGangPlayer(this);
    }

    public void setGangID(UUID gangUUID) {
        if(gangUUID == null) {
            this.gangUUID = null;
            serialize();
            return;
        }
        if(gangUUID.toString().equalsIgnoreCase("")){
            gangUUID = null;
        }
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
        if(getOfflinePlayer().isOnline()) {
            ChatUtil.sendMessage(getOfflinePlayer().getPlayer(), Messages.inviteReceivedFrom(gang.getName()));
        }
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
        ZGangs.getThreadPool().execute(() -> {
            Document document = Document.parse(toJson());
            ZGangs.getDatabase().getGangPlayerCollection()
                    .replaceOne(Filters.eq("_id",
                                    this.uuid.toString()),
                            document,
                            new ReplaceOptions().upsert(true));
        });
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

    public GangPlayerSettings getGangPlayerSettings() {
        return gangPlayerSettings;
    }

    public boolean gangInvitesContains(UUID gangUUID) {
        return gangInvites.contains(gangUUID);
    }

    public boolean isInGang(){
        return this.gangUUID != null;
    }

}
