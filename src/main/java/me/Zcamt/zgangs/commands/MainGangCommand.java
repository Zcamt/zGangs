package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Zcamt.zgangs.helpers.Messages;
import me.Zcamt.zgangs.helpers.Utilities;
import me.Zcamt.zgangs.managers.GangManager;
import me.Zcamt.zgangs.managers.GangPlayerManager;
import me.Zcamt.zgangs.objects.GangPlayer;
import org.bukkit.entity.Player;


@CommandAlias("Gang|Gangs")
public class MainGangCommand extends BaseCommand {

    private final GangManager gangManager;
    private final GangPlayerManager gangPlayerManager;

    public MainGangCommand(GangManager gangManager, GangPlayerManager gangPlayerManager) {
        this.gangManager = gangManager;
        this.gangPlayerManager = gangPlayerManager;
    }

    @Default
    @CatchUnknown
    public void onDefault(Player player){
        Utilities.sendMessage(player, "&cError: Unknown command, please use /gang help");
    }

    @Subcommand("Help")
    public void onHelp(Player player, String[] args){
        Utilities.sendCenteredMessage(player, "&a&l━━━━━━━ &6&lzGangs &a&l━━━━━━━");
        Utilities.sendCenteredMessage(player, "&a[] &f= &7optional arguments");
        Utilities.sendCenteredMessage(player, "&c<> &f= &7required arguments");
        Utilities.sendMessage(player, "");
        Utilities.sendMessage(player, "&a/gang help &f- &7Shows this menu");
        Utilities.sendMessage(player, "&a/gang create &f- &7Used to create a gang");
    }

    //Todo: Handle logic and messages, usage etc for subcommands
    @Subcommand("Create")
    public void onCreate(Player player, String[] args){
        if(args.length != 1) {
            Utilities.sendMessage(player, Messages.INVALID_USAGE.getMessage().replace("{usage}", "/gang create [NAME]"));
        }
        String gangName = args[0];
        GangPlayer gangPlayer = gangPlayerManager.getGangPlayer(player);
        if(gangManager.createNewGang(gangPlayer, gangName) == null){
            player.sendMessage("&cError: Something went wrong when trying to create your new gang");
            return;
        }
        player.sendMessage("&aSuccessfully created your new gang '&c" + gangName + "&a'");
    }

}
