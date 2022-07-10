package me.Zcamt.zgangs.api;

import me.Zcamt.zgangs.ZGangs;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {

    private final ZGangs plugin;

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

        /* %zgangs_gang_name%
        if(identifier.equalsIgnoreCase("zgangs_gang_name")){

        }*/

        return null;
    }

}
