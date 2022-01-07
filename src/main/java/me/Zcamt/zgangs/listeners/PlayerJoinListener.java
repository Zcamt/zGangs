package me.Zcamt.zgangs.listeners;

import me.Zcamt.zgangs.managers.GangManager;
import me.Zcamt.zgangs.managers.GangPlayerManager;
import me.Zcamt.zgangs.objects.GangPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {

    private final GangPlayerManager gangPlayerManager;
    private final GangManager gangManager;

    public PlayerJoinListener(GangPlayerManager gangPlayerManager, GangManager gangManager) {
        this.gangPlayerManager = gangPlayerManager;
        this.gangManager = gangManager;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        GangPlayer gangPlayer;
        gangPlayer = gangPlayerManager.getGangPlayer(player.getUniqueId());
        gangManager.createNewGang(gangPlayer, "test");
        player.sendMessage("Your gang-id is: " + gangPlayer.getGangID() + " and your gang-rank is: " + gangPlayer.getGangRank());
    }

}
