package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.guis.MainGui;
import me.Zcamt.zgangs.guis.NoGangGui;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.PermissionUtil;
import me.Zcamt.zgangs.utils.Permissions;
import org.bukkit.entity.Player;

@CommandAlias("b|bande")
@Conditions("is-player")
public class MenuCommand extends BaseCommand {

    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    @CatchUnknown
    @Default
    public void onDefault(Player player) {
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        if (gangPlayer.isInGang()) {
            MainGui mainGui = new MainGui(player);
            mainGui.openTo(player);
        } else {
            NoGangGui noGangGui = new NoGangGui(player);
            noGangGui.openTo(player);
        }
    }

}
