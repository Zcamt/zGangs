package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.guis.MainGui;
import me.Zcamt.zgangs.guis.NoGangGui;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import org.bukkit.entity.Player;

@CommandAlias("b|bande")
@Conditions("is-player")
public class MenuCommand extends BaseCommand {

    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final GangManager gangManager = ZGangs.getGangManager();

    @CatchUnknown
    @Default
    public void onDefault(Player player) {
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        if (gangPlayer.isInGang()) {
            Gang playerGang = gangManager.findById(gangPlayer.getGangUUID());
            MainGui mainGui = new MainGui(player, playerGang);
            mainGui.openTo(player);
        } else {
            NoGangGui noGangGui = new NoGangGui(player);
            noGangGui.openTo(player);
        }
    }

}
