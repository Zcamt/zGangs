package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Zcamt.zgangs.guis.MainGui;
import me.Zcamt.zgangs.utils.PermissionUtil;
import me.Zcamt.zgangs.utils.Permissions;
import org.bukkit.entity.Player;

@CommandAlias("b|bande")
public class MenuCommand extends BaseCommand {

    private final String permission = Permissions.PLAYER.getPermission();

    @CatchUnknown
    @Default
    public void onDefault(Player player){
        if(!PermissionUtil.hasPermissionWithMessage(player, permission, null)) return;
        MainGui mainGui = new MainGui(player.getUniqueId());
        mainGui.openTo(player);
    }

}
