package me.Zcamt.zgangs.objects;

import me.Zcamt.zgangs.helpers.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

public class GangPlayer {

    private Player player;
    private int gangID;
    private int gangRank;
    private List<Integer> gangInvites;
    //Todo: Add invite logic, could also be temp invites stored in cache for X min

    public GangPlayer(Player player, int gangID, int gangRank, List<Integer> gangInvites) {
        this.player = player;
        this.gangID = gangID;
        this.gangRank = gangRank;
        this.gangInvites = gangInvites;
    }

    public Player getPlayer() {
        return player;
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

    public String getSerializedGangInvitesList(){
        return Utilities.serializeIntListToString(gangInvites);
    }
}
