package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.managers.GangManager;
import me.Zcamt.zgangs.utils.Permissions;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.PermissionUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("bk")
public class MainCommand extends BaseCommand {

    private final String permission = Permissions.PLAYER.getPermission();
    private final GangManager gangManager;

    public MainCommand(GangManager gangManager) {
        this.gangManager = gangManager;
    }

    @CatchUnknown
    public void onUnknown(Player player){
        if(!PermissionUtil.hasPermissionWithMessage(player, permission, null)) return;
        ChatUtil.sendMessage(player, Messages.unknownCommand());
    }

    @Default
    @Subcommand("help|hjælp")
    public void onHelp(Player player){
        if(!PermissionUtil.hasPermissionWithMessage(player, permission, null)) return;
        ChatUtil.sendCenteredMessage(player, Arrays.asList(
                "&a&l━━━━━━━ &6&lzGangs By Zcamt &a&l━━━━━━━",
                "&a[] &f= &7valgfrit",
                "&c<> &f= &7påkrævet"
        ));
        ChatUtil.sendMessage(player, Arrays.asList(
                "&a/b &f- &7Åbner bandemenuen",
                "&a/bk hjælp &f- &7Viser denne besked",
                "&a/bk opret <NAVN>&f- &7Opret din egen bande"
        ));
    }

    @Subcommand("create|opret")
    public void onCreate(Player player, String[] args){
        if(args.length != 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk opret <NAVN>"));
        }
        String name = args[0];

        if(true) { //Check if name is allowed
            gangManager.createNewGang(name, player.getUniqueId());
        } else {
            ChatUtil.sendMessage(player, Messages.illegalGangName());
        }

    }

}
