package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.entity.Player;

@CommandAlias("gang|g")
public class MainCommand extends BaseCommand {


    @CatchUnknown
    public void onUnknown(Player player){
        ChatUtil.sendMessage(player, "");
    }

    @Default
    public void onDefault(Player player){
        
    }

    @Subcommand("help")
    public void onHelp(Player player){
        ChatUtil.sendCenteredMessage(player, "&a&l━━━━━━━ &6&lzGangs &a&l━━━━━━━");
        ChatUtil.sendCenteredMessage(player, "&a[] &f= &7optional arguments");
        ChatUtil.sendCenteredMessage(player, "&c<> &f= &7required arguments");
        ChatUtil.sendMessage(player, "");
        ChatUtil.sendMessage(player, "&a/gang help &f- &7Shows this menu");
        ChatUtil.sendMessage(player, "&a/gang create <NAME>&f- &7Used to create a gang");
    }

}
