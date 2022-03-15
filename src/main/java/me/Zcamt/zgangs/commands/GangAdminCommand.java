package me.Zcamt.zgangs.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Zcamt.zgangs.internals.Permissions;
import me.Zcamt.zgangs.utils.PermissionUtil;
import org.bukkit.entity.Player;

@CommandAlias("gangadmin")
public class GangAdminCommand extends BaseCommand {

    private final String permission = Permissions.ADMIN.getPermission();

    @Default
    public void onDefault(Player player){
        if(!PermissionUtil.hasPermissionWithMessage(player, permission, null)) return;
    }

}
