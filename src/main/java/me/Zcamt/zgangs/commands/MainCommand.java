package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.guis.MainGui;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermission;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.Permissions;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.PermissionUtil;
import me.Zcamt.zgangs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("bk")
@Conditions("is-player")
public class MainCommand extends BaseCommand {

    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final GangManager gangManager = ZGangs.getGangManager();

    @Default
    @Subcommand("help|hjælp")
    public void onHelp(Player player){
        ChatUtil.sendCenteredMessage(player, Arrays.asList(
                "&a&l━━━━━━━ &6&lzGangs af Zcamt &a&l━━━━━━━",
                "&a[] &f= &7valgfrit",
                "&c<> &f= &7påkrævet"
        ));
        ChatUtil.sendMessage(player, Arrays.asList(
                "&a/b &f- &7Åbner bandemenuen",
                "&a/bk hjælp &f- &7Viser denne besked",
                "&a/bk opret <NAVN>&f- &7Opret din egen bande",
                "&a/bk invite <SPILLER>&f- &7Inviter en spiller til din bande"
        ));
    }

    @Subcommand("create|opret")
    @Conditions("no-gang")
    public void onCreate(Player player, String[] args) {
        if(args.length != 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk opret <NAVN>"));
            return;
        }
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        String name = args[0];

        if(Utils.isNameValid(name)) {
            Gang gang = gangManager.createNewGang(name, gangPlayer);
            //Todo: take money
            ChatUtil.sendMessage(player, Config.prefix + " &a&lDu har nu oprettet &c&l" + gang.getName() + " &a&l- Tillykke med din nye bande!");
        } else {
            ChatUtil.sendMessage(player, Messages.illegalGangName);
        }

    }

    @Subcommand("invite|inviter")
    @Conditions("in-gang")
    @CommandCompletion("@players")
    public void onInvite(Player player, String[] args) {
        if(args.length != 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk invite <NAVN>"));
            return;
        }
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        Gang gang = gangManager.findById(gangPlayer.getGangUUID());

        String targetName = args[0];
        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
        if(!offlineTarget.hasPlayedBefore()) {
            ChatUtil.sendMessage(player, Messages.invalidPlayer);
            return;
        }
        GangRank rankRequiredToInvite = gang.getGangPermissions().getRankRequired(GangPermission.INVITE_PLAYERS);

        if(!GangRank.higherThanOrEqual(gangPlayer.getGangRank(), rankRequiredToInvite)) {
            ChatUtil.sendMessage(player, Messages.neededGangRank(rankRequiredToInvite.getName()));
            return;
        }

        GangPlayer targetGangPlayer = gangPlayerManager.findById(offlineTarget.getUniqueId());
        if(gang.getGangMembers().addPlayerToInvites(targetGangPlayer)) {
            ChatUtil.sendMessage(player, Config.prefix + " &aInvitation sendt til " + offlineTarget.getName());
        } else {
            ChatUtil.sendMessage(player, Messages.unexpectedError);
        }
    }


    @Subcommand("menu")
    public void onDefault(Player player){
        MainGui mainGui = new MainGui(player);
        mainGui.openTo(player);
    }

    @CatchUnknown
    public void onUnknown(Player player){
        ChatUtil.sendMessage(player, Messages.unknownCommand);
    }

}
