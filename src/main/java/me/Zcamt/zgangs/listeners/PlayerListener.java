package me.Zcamt.zgangs.listeners;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!gangPlayerManager.idExistsInDatabase(player.getUniqueId())) {
            gangPlayerManager.createNewGangPlayer(player.getUniqueId());
        }
    }
}
