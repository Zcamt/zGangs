package me.Zcamt.zgangs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.guis.MainGui;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermission;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("bk")
@Conditions("is-player")
public class MainCommand extends BaseCommand {

    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final GangManager gangManager = ZGangs.getGangManager();
    private final Economy economy = ZGangs.getEconomy();

    @Default
    @Subcommand("help|hjælp")
    public void onHelp(Player player){
        ChatUtil.sendCenteredMessage(player, Arrays.asList(
                "&a&l━━━━━━━ &6&lzGangs af Zcamt &a&l━━━━━━━",
                "&a[] &f= &7valgfrit",
                "&c<> &f= &7påkrævet"
        ));
        ChatUtil.sendMessage(player, Arrays.asList(
                "&a/b &f- &7Åbner bandemenuen",
                "&a/bk hjælp &f- &7Viser denne besked",
                "&a/bk opret <NAVN>&f- &7Opret din egen bande",
                "&a/bk invite <SPILLER>&f- &7Inviter en spiller til din bande",
                "&a/bk accept <BANDE>&f- &7Accepter en invitation fra en bande",
                "&a/bk forlad <MÆNGDE>&f- &7Forlad din nuværende bande",
                "&a/bk ally <BANDE>&f- &7Send en invitation til en anden bande om at blive allieret",
                "&a/bk rival <BANDE>&f- &7Gør en anden bande til rival"
        ));
    }

    @Subcommand("create|opret")
    @Conditions("no-gang")
    public void onCreate(Player player, String[] args) {
        if(args.length != 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk opret <NAVN>"));
            return;
        }
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        String name = args[0];

        if(!economy.has(player, Config.createGangCost)) {
            ChatUtil.sendMessage(player, Messages.noEnoughMoney);
            return;
        }

        if(Utils.isNameValid(name)) {
            EconomyResponse response = economy.withdrawPlayer(player, Config.createGangCost);
            if(!response.transactionSuccess()) {
                ChatUtil.sendMessage(player, response.errorMessage);
                return;
            }
            Gang gang = gangManager.createNewGang(name, gangPlayer);
            ChatUtil.sendMessage(player, Config.prefix + " &a&lDu har nu oprettet &c&l" + gang.getName() + " &a&l- Tillykke med din nye bande!");
        } else {
            ChatUtil.sendMessage(player, Messages.illegalGangName);
        }

    }

    @Subcommand("invite|inviter")
    @Conditions("in-gang")
    @CommandCompletion("@players")
    public void onInvite(Player player, String[] args) {
        if(args.length != 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk invite <NAVN>"));
            return;
        }
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        Gang gang = gangManager.findById(gangPlayer.getGangUUID());

        String targetName = args[0];
        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
        if(!offlineTarget.hasPlayedBefore()) {
            ChatUtil.sendMessage(player, Messages.invalidPlayer);
            return;
        }
        GangRank rankRequiredToInvite = gang.getGangPermissions().getRankRequired(GangPermission.INVITE_PLAYERS);

        if(!(gangPlayer.getGangRank().compare(rankRequiredToInvite) >= 0)) {
            ChatUtil.sendMessage(player, Messages.neededGangRank(rankRequiredToInvite.getName()));
            return;
        }

        GangPlayer targetGangPlayer = gangPlayerManager.findById(offlineTarget.getUniqueId());
        if(gang.getGangMembers().addPlayerToInvites(targetGangPlayer)) {
            ChatUtil.sendMessage(player, Config.prefix + " " + Messages.inviteSentToPlayer(offlineTarget.getName()));
        } else {
            ChatUtil.sendMessage(player, Messages.unexpectedError);
        }
    }

    @Subcommand("accept|accepter")
    @Conditions("no-gang")
    public void onAccept(Player player, String[] args) {
        if(args.length != 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk accept <BANDE>"));
            return;
        }
        String targetGangName = args[0];
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        Gang gang = gangManager.findByName(targetGangName);
        if(gang == null) {
            ChatUtil.sendMessage(player, Messages.invalidGang);
            return;
        }

        if(gang.getGangMembers().isInvited(player.getUniqueId())) {
            if (gang.getGangMembers().addGangPlayerToGang(gangPlayer)) {
                gang.sendMessageToOnlineMembers(Messages.playerJoinedGang(player.getName()));
            }
        } else {
            ChatUtil.sendMessage(player, Messages.invalidGang);
        }
    }

    @Subcommand("bank")
    @Conditions("in-gang")
    public void onDeposit(Player player, String[] args) {
        if(args.length != 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk bank <MÆNGDE>"));
            return;
        }
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        Gang gang = gangManager.findById(gangPlayer.getGangUUID());

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            ChatUtil.sendMessage(player, Messages.invalidInput);
            return;
        }

        if(!economy.has(player, amount)) {
            ChatUtil.sendMessage(player, Messages.noEnoughMoney);
            return;
        }

        EconomyResponse response = economy.withdrawPlayer(player, amount);
        if(!response.transactionSuccess()) {
            ChatUtil.sendMessage(player, response.errorMessage);
            return;
        }

        gang.setBank(gang.getBank() + amount);
        ChatUtil.sendMessage(player, Config.prefix + " " + Messages.bankDeposit(amount));
    }

    @Subcommand("forlad|leave")
    @Conditions("in-gang")
    public void onLeave(Player player, String[] args) {
        if(args.length != 0){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk forlad"));
            return;
        }

        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        Gang gang = gangManager.findById(gangPlayer.getGangUUID());

        if(gangPlayer.getGangRank().compare(GangRank.OWNER) >= 0) {
            ChatUtil.sendMessage(player, Messages.cantLeaveAsOwner);
            return;
        }

        if(gang.getGangMembers().removeGangPlayerFromGang(gangPlayer)) {
            gang.sendMessageToOnlineMembers(Messages.playerLeftGang(player.getName()));
            ChatUtil.sendMessage(player, Config.prefix + " &a&lDu har forladt din bande");
        } else {
            ChatUtil.sendMessage(player, Messages.unexpectedError);
        }
    }

    @Subcommand("ally")
    @Conditions("in-gang")
    public void onAlly(Player player, String[] args) {
        if(args.length != 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk ally <BANDE>"));
            return;
        }
        String targetGangName = args[0];
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        Gang playerGang = gangManager.findById(gangPlayer.getGangUUID());
        Gang targetGang = gangManager.findByName(targetGangName);

        if(playerGang.getGangAllies().addAllyInviteOutgoing(targetGang)) {
            playerGang.sendMessageToOnlineMembers(Messages.allyInviteOutgoing(targetGang.getName()));
            targetGang.sendMessageToOnlineMembers(Messages.allyInviteIncoming(playerGang.getName()));
        } else {
            //Todo: Bliver også sendt når banden allerede har modtaget en ally invite, måske en ny slags fejlbesked eller et ekstra tjek ovenover
            ChatUtil.sendMessage(player, Messages.unexpectedError);
        }

    }

    @Subcommand("rival")
    @Conditions("in-gang")
    public void onRival(Player player, String[] args) {
        if(args.length != 1){
            ChatUtil.sendMessage(player, Messages.invalidUsage("/bk rival <BANDE>"));
            return;
        }
        String targetGangName = args[0];
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        Gang playerGang = gangManager.findById(gangPlayer.getGangUUID());
        Gang targetGang = gangManager.findByName(targetGangName);

        if(playerGang.getGangRivals().addRival(targetGang)) {
            playerGang.sendMessageToOnlineMembers(Messages.allyInviteOutgoing(targetGang.getName()));
            targetGang.sendMessageToOnlineMembers(Messages.allyInviteIncoming(playerGang.getName()));
        } else {
            //Todo: Bliver også sendt når banden allerede er markeret som rival, måske en ny slags fejlbesked eller et ekstra tjek ovenover
            ChatUtil.sendMessage(player, Messages.unexpectedError);
        }

    }

    @Subcommand("menu")
    public void onDefault(Player player){
        MainGui mainGui = new MainGui(player);
        mainGui.openTo(player);
    }

    @CatchUnknown
    public void onUnknown(Player player){
        ChatUtil.sendMessage(player, Messages.unknownCommand);
    }

}
