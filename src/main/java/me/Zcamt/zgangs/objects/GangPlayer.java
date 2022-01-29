package me.Zcamt.zgangs.objects;

import me.Zcamt.zgangs.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class GangPlayer {

    //Todo: Might wanna change to only use UUIDs as that allows for creation of GangPlayer object without player being online.

    //Todo: Update gangPlayer object in database during set methods

    private UUID uuid;
    private int gangID;
    private int gangRank;
    private List<Integer> gangInvites;

    public GangPlayer(UUID uuid, int gangID, int gangRank, List<Integer> gangInvites) {
        this.uuid = uuid;
        this.gangID = gangID;
        this.gangRank = gangRank;
        this.gangInvites = gangInvites;
    }

    public UUID getUUID() {
        return uuid;
    }
    public OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(uuid);
    }

    public int getGangID() {
        return gangID;
    }
    public void setGangID(int gangID) {
        this.gangID = gangID;
    }

    public int getGangRank() {
        return gangRank;
    }
    public void setGangRank(int gangRank) {
        this.gangRank = gangRank;
    }

    public void addGangInvite(int gangID) {
        gangInvites.add(gangID);
    }
    public void removeGangInvite(int gangID) {
        gangInvites.remove(gangID);
    }
    public boolean gangInvitesContains(int gangID) {
        return gangInvites.contains(gangID);
    }

    public String getSerializedGangInvitesList(){
        return Utilities.serializeIntListToString(gangInvites);
    }
}
