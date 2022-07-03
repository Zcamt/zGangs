package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@CommandAlias("bandechat")
@Conditions("is-player|in-gang")
public class GangChatCommand extends BaseCommand {

    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final GangManager gangManager = ZGangs.getGangManager();

    @Default
    @CatchUnknown
    public void onGangChat(Player player, String[] args){
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        Gang playerGang = gangManager.findById(gangPlayer.getGangUUID());

        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> messageIterator = Arrays.stream(args).iterator();
        while (messageIterator.hasNext()) {
            String messageWord = messageIterator.next();
            stringBuilder.append(messageWord);
            if(messageIterator.hasNext()){
                stringBuilder.append(" ");
            }
        }

        List<UUID> gangMembers = playerGang.getGangMembers().getMemberList();

        for (UUID memberUUID : gangMembers) {
            OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(memberUUID);
            if(!offlineMember.isOnline()) continue;
            GangPlayer gangMember = gangPlayerManager.findById(memberUUID);
            if(gangMember.getGangPlayerSettings().isReceiveGangChat()){
                ChatUtil.sendMessage(gangMember.getOfflinePlayer().getPlayer(), stringBuilder.toString());
            }
        }

    }

}
