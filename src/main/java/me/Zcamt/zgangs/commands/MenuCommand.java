package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.guis.ExternalGangGui;
import me.Zcamt.zgangs.guis.MainGui;
import me.Zcamt.zgangs.guis.NoGangGui;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("b|bande")
@Conditions("is-player")
public class MenuCommand extends BaseCommand {

    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final GangManager gangManager = ZGangs.getGangManager();

    @CatchUnknown
    public void onUnknown(Player player, String[] args) {
        if(args.length >= 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/b <SPILLER>"));
            return;
        }
        String targetPlayerName = args[0];
        OfflinePlayer targetOfflinePlayer = Bukkit.getOfflinePlayer(targetPlayerName);
        if(!targetOfflinePlayer.hasPlayedBefore()) {
            ChatUtil.sendMessage(player, Messages.invalidPlayer);
            return;
        }
        GangPlayer targetGangPlayer = gangPlayerManager.findById(targetOfflinePlayer.getUniqueId());
        if(!targetGangPlayer.isInGang()) {
            ChatUtil.sendMessage(player, Messages.invalidGang);
            return;
        }
        Gang targetGang = gangManager.findById(targetGangPlayer.getGangUUID());
        ExternalGangGui externalGangGui = new ExternalGangGui(player, targetGang);
        externalGangGui.openTo(player);
    }

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
