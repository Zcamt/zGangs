package me.Zcamt.zgangs.api;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {

    private final ZGangs plugin;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    public Placeholders(ZGangs plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier(){
        return "zgangs";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return null;
        }


        //%zgangs_gang_name%
        if(identifier.equalsIgnoreCase("zgangs_gang_name")){
            GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
            if(gangPlayer == null) return "Ingen bande";

            Gang playerGang = gangManager.findById(gangPlayer.getGangUUID());
            if(playerGang == null) return "Ingen bande";

            return playerGang.getName();
        }

        //%zgangs_gang_bank%
        if(identifier.equalsIgnoreCase("zgangs_gang_bank")){
            GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
            if(gangPlayer == null) return "Ingen bande";

            Gang playerGang = gangManager.findById(gangPlayer.getGangUUID());
            if(playerGang == null) return "Ingen bande";

            return String.valueOf(playerGang.getBank());
        }

        //%zgangs_gang_online%
        if(identifier.equalsIgnoreCase("zgangs_gang_online")){
            GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
            if(gangPlayer == null) return String.valueOf(0);

            Gang playerGang = gangManager.findById(gangPlayer.getGangUUID());
            if(playerGang == null) return String.valueOf(0);

            return String.valueOf(playerGang.getGangMembers().getOnlineMembers().size());
        }

        //%zgangs_gangplayer_rank%
        if(identifier.equalsIgnoreCase("zgangs_gangplayer_rank")){
            GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
            if(gangPlayer == null) return "Ingen rang";
            if(!gangPlayer.isInGang()) return "Ingen bande";

            return gangPlayer.getGangRank().getName();
        }

        return null;
    }

}
