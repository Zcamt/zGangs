package me.Zcamt.zgangs.guis;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.chatinput.ChatInputManager;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.UUID;

public class ManageMembersGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final ChatInputManager chatInputManager = ZGangs.getChatInputManager();

    protected ManageMembersGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBandens medlemmer"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        Iterator<UUID> gangMemberIterator = gang.getGangMembers().getMemberList().listIterator();

        for (int i = 10; i <= 43; i++) {
            if (gangMemberIterator.hasNext()) {
                GangPlayer gangMember = gangPlayerManager.findById(gangMemberIterator.next());
                ItemStack invitedPlayerItemStack = new ItemCreator(Material.PLAYER_HEAD)
                        .setSkullTextureFromePlayerName(gangMember.getOfflinePlayer().getName())
                        .setName("&a&l" + gangMember.getOfflinePlayer().getName())
                        .addLore("&7Venstre-Klik her for at:",
                                "&6- &fHåndtere dette bandemedlem",
                                "",
                                gangPlayer.getGangRank().isHigherThan(gangMember.getGangRank()) ?
                                        "" : "&cDu er for lav rang til at håndtere dette medlem")
                        .make();

                NBTItem invitedPlayerNbtItem = new NBTItem(invitedPlayerItemStack, true);
                invitedPlayerNbtItem.setUUID("player-uuid", gangMember.getUUID());
                setItem(i, invitedPlayerNbtItem.getItem());
            } else {
                setItem(i, new ItemCreator(Material.NETHER_STAR).setName("&aInviter en spiller")
                        .addLore("&7Klik her for at:",
                                "&6- &fInvitere en ny spiller til banden")
                        .make());
            }
        }

        setItem(40, new ItemCreator(Material.PAPER).setName("&aUdgående invitationer")
                .addLore("&7Klik her for at:",
                        "&6- &fSe udgående invitationer til spillere")
                .make());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                GangSettingsGui gangSettingsGui = new GangSettingsGui(player, gang);
                gangSettingsGui.openTo(player);
            }
            case PAPER -> {
                OutgoingInvitesGui outgoingInvitesGui = new OutgoingInvitesGui(player, gang);
                outgoingInvitesGui.openTo(player);
            }
            case NETHER_STAR -> {
                chatInputManager.newStringInput(player, name -> {
                    OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(name);
                    if(!offlineTarget.hasPlayedBefore()) {
                        ChatUtil.sendMessage(player, Messages.invalidPlayer);
                        return;
                    }
                    GangPlayer targetGangPlayer = gangPlayerManager.findById(offlineTarget.getUniqueId());
                    gang.getGangMembers().addPlayerToInvites(targetGangPlayer);
                });
                ChatUtil.sendMessage(player, Config.prefix + " &a&lSkriv navnet på den spiller du ønsker at invitere! " +
                        "- Hvis du ønsker at afbryde processen tast '&c&l-afbryd&a&l'");
                player.closeInventory();
            }
            case PLAYER_HEAD -> {
                NBTItem clickedNbtItem = new NBTItem(clickedItem);
                UUID clickedPlayerUUID = clickedNbtItem.getUUID("player-uuid");
                if (clickedPlayerUUID == null) return;
                GangPlayer clickedGangPlayer = gangPlayerManager.findById(clickedPlayerUUID);
                if (clickedGangPlayer == null) return;
                if(!gangPlayer.getGangRank().isHigherThan(clickedGangPlayer.getGangRank())) return;
                ManageSingleMemberGui manageSingleMemberGui = new ManageSingleMemberGui(player, gang, clickedGangPlayer);
                manageSingleMemberGui.openTo(player);
            }
        }
    }
}

class OutgoingInvitesGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected OutgoingInvitesGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lUdgående invitationer"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        Iterator<UUID> outgoingInvitesIterator = gang.getGangMembers().getPlayerInvites().listIterator();

        for (int i = 10; i <= 43; i++) {
            if (outgoingInvitesIterator.hasNext()) {
                GangPlayer invitedPlayer = gangPlayerManager.findById(outgoingInvitesIterator.next());
                ItemStack invitedPlayerItemStack = new ItemCreator(Material.PLAYER_HEAD)
                        .setSkullTextureFromePlayerName(invitedPlayer.getOfflinePlayer().getName())
                        .setName("&a&l" + invitedPlayer.getOfflinePlayer().getName())
                        .addLore("&7Venstre-Klik her for at:",
                                "&6- &fTrække invitationen tilbage")
                        .make();

                NBTItem invitedPlayerNbtItem = new NBTItem(invitedPlayerItemStack, true);
                invitedPlayerNbtItem.setUUID("player-uuid", invitedPlayer.getUUID());
                setItem(i, invitedPlayerNbtItem.getItem());
            }
        }

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                ManageMembersGui manageMembersGui = new ManageMembersGui(player, gang);
                manageMembersGui.openTo(player);
            }
            case PLAYER_HEAD -> {
                if(event.isRightClick()) {
                    NBTItem clickedNbtItem = new NBTItem(clickedItem);
                    UUID clickedPlayerUUID = clickedNbtItem.getUUID("player-uuid");
                    if(clickedPlayerUUID == null) return;
                    GangPlayer clickedPlayer = gangPlayerManager.findById(clickedPlayerUUID);
                    if(clickedPlayer == null) return;
                    gang.getGangMembers().removePlayerFromInvites(clickedPlayer);
                    ChatUtil.sendMessage(player, "&aDu har nu fjernet invitationen til " + clickedPlayer.getOfflinePlayer().getName());
                }
            }
        }
    }
}

class ManageSingleMemberGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangPlayer targetGangPlayer;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected ManageSingleMemberGui(Player player, Gang playerGang, GangPlayer targetGangPlayer) {
        super(54, ChatUtil.CC("&c&lBandemedlem: " + targetGangPlayer.getOfflinePlayer().getName()));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        this.targetGangPlayer = targetGangPlayer;

        String targetName = targetGangPlayer.getOfflinePlayer().getName();

        setItem(13, new ItemCreator(Material.PLAYER_HEAD).setName("&c" + targetName)
                .addLore("&cNuværende rang: " + targetGangPlayer.getGangRank().getFormatedNamed(),
                        "&cKills: &f%en_sej_placeholder%",
                        "&cDøde: &f%en_sej_placeholder%",
                        "&cK/D: &f%en_sej_placeholder%")
                .make());

        setItem(20, new ItemCreator(Material.LEATHER_HELMET).setName(GangRank.RECRUIT.getFormatedNamed())
                .addLore("&7Klik her for at:",
                        "&6- &fGøre &c" + targetName + " &ftil " + GangRank.RECRUIT.getFormatedNamed(),
                        "",
                        gangPlayer.getGangRank().isHigherThan(targetGangPlayer.getGangRank()) ?
                                "" : "&cDu er for lav rang til at give denne rang")
                .make());

        setItem(21, new ItemCreator(Material.IRON_HELMET).setName(GangRank.MEMBER.getFormatedNamed())
                .addLore("&7Klik her for at:",
                        "&6- &fGøre &c" + targetName + " &ftil " + GangRank.MEMBER.getFormatedNamed(),
                        "",
                        gangPlayer.getGangRank().isHigherThan(targetGangPlayer.getGangRank()) ?
                                "" : "&cDu er for lav rang til at give denne rang")
                .make());

        setItem(22, new ItemCreator(Material.GOLDEN_HELMET).setName(GangRank.CAPTAIN.getFormatedNamed())
                .addLore("&7Klik her for at:",
                        "&6- &fGøre &c" + targetName + " &ftil " + GangRank.CAPTAIN.getFormatedNamed(),
                        "",
                        gangPlayer.getGangRank().isHigherThan(targetGangPlayer.getGangRank()) ?
                                "" : "&cDu er for lav rang til at give denne rang")
                .make());

        setItem(23, new ItemCreator(Material.CHAINMAIL_HELMET).setName(GangRank.CO_OWNER.getFormatedNamed())
                .addLore("&7Klik her for at:",
                        "&6- &fGøre &c" + targetName + " &ftil " + GangRank.CO_OWNER.getFormatedNamed(),
                        "",
                        gangPlayer.getGangRank().isHigherThan(targetGangPlayer.getGangRank()) ?
                                "" : "&cDu er for lav rang til at give denne rang")
                .make());

        setItem(24, new ItemCreator(Material.DIAMOND_HELMET).setName(GangRank.OWNER.getFormatedNamed())
                .addLore("&7Klik her for at:",
                        "&6- &fGøre &c" + targetName + " &ftil " + GangRank.OWNER.getFormatedNamed(),
                        "",
                        gangPlayer.getGangRank().isHigherThan(targetGangPlayer.getGangRank()) ?
                                "" : "&cDu er for lav rang til at give denne rang")
                .make());

        setItem(31, new ItemCreator(Material.RED_DYE).setName("&cKick medlem")
                .addLore("&7Klik her for at:",
                        "&6- &fSmide &c" + targetName + " &fud af banden")
                .make());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                ManageMembersGui manageMembersGui = new ManageMembersGui(player, gang);
                manageMembersGui.openTo(player);
            }
            case RED_DYE -> {
                if(!gangPlayer.getGangRank().isHigherOrEqualThan(gang.getGangPermissions().getMinRankToKickMembers())) return;
                gang.getGangMembers().removeGangPlayerFromGang(targetGangPlayer);
                gang.sendMessageToOnlineMembers(Messages.playerKickedFromGang(targetGangPlayer.getOfflinePlayer().getName()));
                OfflinePlayer offlinePlayer = targetGangPlayer.getOfflinePlayer();
                if(offlinePlayer.getPlayer() != null && offlinePlayer.isOnline()) {
                    ChatUtil.sendMessage(offlinePlayer.getPlayer(), Config.prefix + "&aDu er blevet smidt ud af din bande");
                }
                ManageMembersGui manageMembersGui = new ManageMembersGui(player, gang);
                manageMembersGui.openTo(player);
            }
            case LEATHER_HELMET -> {
                if(!gangPlayer.getGangRank().isHigherOrEqualThan(gang.getGangPermissions().getMinRankToManageMemberRanks())) return;
                if(!gangPlayer.getGangRank().isHigherThan(GangRank.RECRUIT)) return;
                targetGangPlayer.setGangRank(GangRank.RECRUIT);
            }
            case IRON_HELMET -> {
                if(!gangPlayer.getGangRank().isHigherOrEqualThan(gang.getGangPermissions().getMinRankToManageMemberRanks())) return;
                if(!gangPlayer.getGangRank().isHigherThan(GangRank.MEMBER)) return;
                targetGangPlayer.setGangRank(GangRank.MEMBER);
            }
            case GOLDEN_HELMET -> {
                if(!gangPlayer.getGangRank().isHigherOrEqualThan(gang.getGangPermissions().getMinRankToManageMemberRanks())) return;
                if(!gangPlayer.getGangRank().isHigherThan(GangRank.CAPTAIN)) return;
                targetGangPlayer.setGangRank(GangRank.CAPTAIN);
            }
            case CHAINMAIL_HELMET -> {
                if(!gangPlayer.getGangRank().isHigherOrEqualThan(gang.getGangPermissions().getMinRankToManageMemberRanks())) return;
                if(!gangPlayer.getGangRank().isHigherThan(GangRank.CO_OWNER)) return;
                targetGangPlayer.setGangRank(GangRank.CO_OWNER);
            }
            case DIAMOND_HELMET -> {
                if(gangPlayer.getGangRank() != GangRank.OWNER) return;
                targetGangPlayer.setGangRank(GangRank.OWNER);
            }
        }
    }
}
