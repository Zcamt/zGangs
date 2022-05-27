package me.Zcamt.zgangs.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.Permissions;
import me.Zcamt.zgangs.utils.PermissionUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("bandeadmin|ba")
@Conditions("is-admin")
public class GangAdminCommand extends BaseCommand {

    @Default
    @CatchUnknown
    @Subcommand("help|hjælp")
    public void onHelp(Player player){
        ChatUtil.sendCenteredMessage(player, Arrays.asList(
                "&a&l━━━━━━━ &6&lzGangs af Zcamt &a&l━━━━━━━",
                "&a[] &f= &7valgfrit",
                "&c<> &f= &7påkrævet"
        ));
        ChatUtil.sendMessage(player, Arrays.asList(
                "&a/ba &f- &7Viser denne besked"
        ));
    }

}
