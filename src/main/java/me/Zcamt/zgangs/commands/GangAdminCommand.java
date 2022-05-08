package me.Zcamt.zgangs.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.managers.GangManager;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gangpermissions.GangPermissions;
import me.Zcamt.zgangs.utils.Permissions;
import me.Zcamt.zgangs.utils.PermissionUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@CommandAlias("gangadmin")
public class GangAdminCommand extends BaseCommand {

    private final String permission = Permissions.ADMIN.getPermission();

    @Default
    @CatchUnknown
    public void onDefault(Player player){
        if(!PermissionUtil.hasPermissionWithMessage(player, permission, null)) return;
    }

    /*@Subcommand("test")
    public void onTest(Player player){
        if(!PermissionUtil.hasPermissionWithMessage(player, permission, null)) return;
        GangManager gangManager = ZGangs.getGangManager();
        int i = 0;
        while (i < 25) {
            UUID gangUUID = UUID.randomUUID();
            Gang gang = new Gang(gangUUID, System.currentTimeMillis(), "gang-"+i, 1, 0, 0, 0, 0, 0,
                    Config.defaultMaxMembers, Config.defaultMaxAllies, UUID.randomUUID(), new ArrayList<>(),
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                    new GangPermissions(new HashMap<>()));
            gangManager.addGangToCache(gangUUID, gang);
            player.sendMessage("create " + gang.getName());
            i++;
        }

    }*/

}
