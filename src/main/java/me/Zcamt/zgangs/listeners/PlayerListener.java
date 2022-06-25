package me.Zcamt.zgangs.listeners;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class PlayerListener implements Listener {

    GangManager gangManager = ZGangs.getGangManager();
    GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!gangPlayerManager.idExistsInDatabase(player.getUniqueId())) {
            gangPlayerManager.createNewGangPlayer(player.getUniqueId());
        }
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        if(!gangPlayer.isInGang()) return;

        Gang playerGang = gangManager.findById(gangPlayer.getGangUUID());
        List<UUID> gangMembers = playerGang.getGangMembers().getMemberList();

        for (UUID memberUUID : gangMembers) {
            if(!Bukkit.getOfflinePlayer(memberUUID).isOnline()) continue;

            GangPlayer gangMember = gangPlayerManager.findById(memberUUID);
            if(gangMember.getGangPlayerSettings().isReceiveMemberConnectNotification()){
                ChatUtil.sendMessage(gangMember.getOfflinePlayer().getPlayer(), Messages.memberConnected(player.getName()));
            }

        }

        List<UUID> alliedGangs = playerGang.getGangAllies().getAlliedGangs();
        for (UUID alliedGangUUID : alliedGangs) {
            Gang alliedGang = gangManager.findById(alliedGangUUID);
            List<UUID> alliedGangMembers = alliedGang.getGangMembers().getMemberList();

            for (UUID alliedGangMemberUUID : alliedGangMembers) {
                if(!Bukkit.getOfflinePlayer(alliedGangMemberUUID).isOnline()) continue;

                GangPlayer gangMember = gangPlayerManager.findById(alliedGangMemberUUID);
                if(gangMember.getGangPlayerSettings().isReceiveAllyConnectNotification()){
                    ChatUtil.sendMessage(gangMember.getOfflinePlayer().getPlayer(), Messages.allyConnected(player.getName()));
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        if(!gangPlayer.isInGang()) return;

        Gang playerGang = gangManager.findById(gangPlayer.getGangUUID());
        List<UUID> gangMembers = playerGang.getGangMembers().getMemberList();

        for (UUID memberUUID : gangMembers) {
            if(!Bukkit.getOfflinePlayer(memberUUID).isOnline()) continue;

            GangPlayer gangMember = gangPlayerManager.findById(memberUUID);
            if(gangMember.getGangPlayerSettings().isReceiveMemberDisconnectNotification()){
                ChatUtil.sendMessage(gangMember.getOfflinePlayer().getPlayer(), Messages.memberDisconnected(player.getName()));
            }

        }

        List<UUID> alliedGangs = playerGang.getGangAllies().getAlliedGangs();
        for (UUID alliedGangUUID : alliedGangs) {
            Gang alliedGang = gangManager.findById(alliedGangUUID);
            List<UUID> alliedGangMembers = alliedGang.getGangMembers().getMemberList();

            for (UUID alliedGangMemberUUID : alliedGangMembers) {
                if(!Bukkit.getOfflinePlayer(alliedGangMemberUUID).isOnline()) continue;

                GangPlayer gangMember = gangPlayerManager.findById(alliedGangMemberUUID);
                if(gangMember.getGangPlayerSettings().isReceiveAllyDisconnectNotification()){
                    ChatUtil.sendMessage(gangMember.getOfflinePlayer().getPlayer(), Messages.allyDisconnected(player.getName()));
                }
            }
        }
    }

}
