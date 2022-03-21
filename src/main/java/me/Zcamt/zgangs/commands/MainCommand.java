package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.Zcamt.zgangs.guis.GuiTest;
import me.Zcamt.zgangs.guis.MainGui;
import me.Zcamt.zgangs.internals.Messages;
import me.Zcamt.zgangs.internals.Permissions;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.PermissionUtil;
import org.bukkit.entity.Player;

@CommandAlias("gang|g")
public class MainCommand extends BaseCommand {

    private final String permission = Permissions.PLAYER.getPermission();

    @CatchUnknown
    public void onUnknown(Player player){
        if(!PermissionUtil.hasPermissionWithMessage(player, permission, null)) return;
        ChatUtil.sendMessage(player, Messages.unknownCommand());
    }

    @Default
    public void onDefault(Player player){
        if(!PermissionUtil.hasPermissionWithMessage(player, permission, null)) return;
        MainGui mainGui = new MainGui(player.getUniqueId());
        mainGui.openTo(player);
    }

    @Subcommand("help")
    public void onHelp(Player player){
        if(!PermissionUtil.hasPermissionWithMessage(player, permission, null)) return;
        ChatUtil.sendCenteredMessage(player, "&a&l━━━━━━━ &6&lzGangs &a&l━━━━━━━");
        ChatUtil.sendCenteredMessage(player, "&a[] &f= &7optional arguments");
        ChatUtil.sendCenteredMessage(player, "&c<> &f= &7required arguments");
        ChatUtil.sendMessage(player, "");
        ChatUtil.sendMessage(player, "&a/gang help &f- &7Shows this message");
        ChatUtil.sendMessage(player, "&a/gang create <NAME>&f- &7Used to create a gang");
    }

}
