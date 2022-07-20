package me.Zcamt.zgangs.guis;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.UUID;

public class ManageAlliesGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected ManageAlliesGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBandens allierede"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());


        setItem(20, new ItemCreator(Material.PAPER).setName("&aIndgående invitationer")
                .addLore("&7Klik her for at:",
                        "&6- &fSe indgående invitationer",
                        "&6- &fHåndtere indgående invitationer")
                .make());

        setItem(22, new ItemCreator(Material.GREEN_BANNER).setName("&aNuværende allierede")
                .addLore("&7Klik her for at:",
                        "&6- &fSe nuværende allierede",
                        "&6- &fHåndtere nuværende allierede")
                .make());

        setItem(24, new ItemCreator(Material.PAPER).setName("&aUdgående invitationer")
                .addLore("&7Klik her for at:",
                        "&6- &fSe udgående invitationer",
                        "&6- &fHåndtere udgående invitationer")
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
            case GREEN_BANNER -> {
                CurrentAlliesGui currentAlliesGui = new CurrentAlliesGui(player, gang);
                currentAlliesGui.openTo(player);
            }
            case PAPER -> {
                if(clickedItem.getItemMeta() == null) return;
                String clickedItemName = clickedItem.getItemMeta().getDisplayName();
                if(clickedItemName.contains("Indgående invitationer")) {

                } else if(clickedItemName.contains("Udgående invitationer")) {

                }
            }
        }
    }
}

class CurrentAlliesGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected CurrentAlliesGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lNuværende allierede"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        Iterator<UUID> alliedGangIterator = gang.getGangAllies().getAlliedGangs().listIterator();

        for (int i = 10; i <= 43; i++) {
            if(alliedGangIterator.hasNext()) {
                Gang alliedGang = gangManager.findById(alliedGangIterator.next());
                ItemStack allyItemStack = new ItemCreator(Material.PLAYER_HEAD)
                        .setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(alliedGang.getOwnerUUID()).getName())
                        .setName("&a&l" + alliedGang.getName())
                        .addLore("&7Højre-Klik her for at:",
                                "&6- &fSe information om banden",
                                "",
                                "&7Venstre-Klik her for at:",
                                "&6- &fFjerne banden som allieret")
                        .make();

                NBTItem allyNbtItem = new NBTItem(allyItemStack, true);
                allyNbtItem.setUUID("gang-uuid", alliedGang.getUUID());
                setItem(i, allyNbtItem.getItem());
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
                ManageAlliesGui manageAlliesGui = new ManageAlliesGui(player, gang);
                manageAlliesGui.openTo(player);
            }
            case PLAYER_HEAD -> {
                if(event.isLeftClick()) {
                    NBTItem clickedNbtItem = new NBTItem(clickedItem);
                    UUID clickedGangUUID = clickedNbtItem.getUUID("gang-uuid");
                    if(clickedGangUUID == null) return;
                    Gang clickedGang = gangManager.findById(clickedGangUUID);
                    if(clickedGang == null) return;
                    GangInfoGui gangInfoGui = new GangInfoGui(player, clickedGang);
                    gangInfoGui.openTo(player);

                } else if (event.isRightClick()) {
                    NBTItem clickedNbtItem = new NBTItem(clickedItem);
                    UUID clickedGangUUID = clickedNbtItem.getUUID("gang-uuid");
                    if(clickedGangUUID == null) return;
                    Gang clickedGang = gangManager.findById(clickedGangUUID);
                    if(clickedGang == null) return;
                    if(gang.getGangAllies().removeAlly(clickedGang)) {
                        gang.sendMessageToOnlineMembers(Messages.noLongerAlly(clickedGang.getName()));
                        clickedGang.sendMessageToOnlineMembers(Messages.noLongerAlly(gang.getName()));

                        CurrentAlliesGui currentAlliesGui = new CurrentAlliesGui(player, gang);
                        currentAlliesGui.openTo(player);
                    }
                }
            }
        }
    }

}

class IncomingAllyInvitesGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected IncomingAllyInvitesGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lIndgående ally-invitationer"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        Iterator<UUID> incomingInvitationsIterator = gang.getGangAllies().getAlliedGangInvitesIncoming().listIterator();

        for (int i = 10; i <= 43; i++) {
            if(incomingInvitationsIterator.hasNext()) {
                Gang inviteSenderGang = gangManager.findById(incomingInvitationsIterator.next());
                ItemStack allyItemStack = new ItemCreator(Material.PLAYER_HEAD)
                        .setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(inviteSenderGang.getOwnerUUID()).getName())
                        .setName("&a&l" + inviteSenderGang.getName())
                        .addLore("&7Højre-Klik her for at:",
                                "&6- &fAcceptere invitationen",
                                "",
                                "&7Venstre-Klik her for at:",
                                "&6- &fAfvise invitationen")
                        .make();

                NBTItem allyNbtItem = new NBTItem(allyItemStack, true);
                allyNbtItem.setUUID("gang-uuid", inviteSenderGang.getUUID());
                setItem(i, allyNbtItem.getItem());
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
                ManageAlliesGui manageAlliesGui = new ManageAlliesGui(player, gang);
                manageAlliesGui.openTo(player);
            }
            case PLAYER_HEAD -> {
                if(event.isLeftClick()) {
                    NBTItem clickedNbtItem = new NBTItem(clickedItem);
                    UUID clickedGangUUID = clickedNbtItem.getUUID("gang-uuid");
                    if(clickedGangUUID == null) return;
                    Gang clickedGang = gangManager.findById(clickedGangUUID);
                    if(clickedGang == null) return;

                    if(gang.getGangAllies().addAlly(clickedGang)) {
                        gang.sendMessageToOnlineMembers(Messages.newAlly(clickedGang.getName()));
                        clickedGang.sendMessageToOnlineMembers(Messages.newAlly(gang.getName()));
                    } else {
                        //Todo: Change message at somepoint
                        ChatUtil.sendMessage(player, Messages.unexpectedError);
                    }
                    IncomingAllyInvitesGui incomingAllyInvitesGui = new IncomingAllyInvitesGui(player, gang);
                    incomingAllyInvitesGui.openTo(player);

                } else if (event.isRightClick()) {
                    NBTItem clickedNbtItem = new NBTItem(clickedItem);
                    UUID clickedGangUUID = clickedNbtItem.getUUID("gang-uuid");
                    if(clickedGangUUID == null) return;
                    Gang clickedGang = gangManager.findById(clickedGangUUID);
                    if(clickedGang == null) return;
                    if(!gang.getGangAllies().removeAllyInviteIncoming(clickedGang)) {
                        //Todo: Change message at somepoint
                        ChatUtil.sendMessage(player, Messages.unexpectedError);
                    }
                    IncomingAllyInvitesGui incomingAllyInvitesGui = new IncomingAllyInvitesGui(player, gang);
                    incomingAllyInvitesGui.openTo(player);
                }
            }
        }
    }
}

class OutgoingAllyInvitesGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected OutgoingAllyInvitesGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lUdgående ally-invitationer"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        Iterator<UUID> outgoingInvitationsIterator = gang.getGangAllies().getAlliedGangInvitesOutgoing().listIterator();

        for (int i = 10; i <= 43; i++) {
            if(outgoingInvitationsIterator.hasNext()) {
                Gang inviteReceiverGang = gangManager.findById(outgoingInvitationsIterator.next());
                ItemStack allyItemStack = new ItemCreator(Material.PLAYER_HEAD)
                        .setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(inviteReceiverGang.getOwnerUUID()).getName())
                        .setName("&a&l" + inviteReceiverGang.getName())
                        .addLore("&7Højre-Klik her for at:",
                                "&6- &fSe information om banden",
                                "",
                                "&7Venstre-Klik her for at:",
                                "&6- &fFjerne banden som allieret")
                        .make();

                NBTItem allyNbtItem = new NBTItem(allyItemStack, true);
                allyNbtItem.setUUID("gang-uuid", inviteReceiverGang.getUUID());
                setItem(i, allyNbtItem.getItem());
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
                ManageAlliesGui manageAlliesGui = new ManageAlliesGui(player, gang);
                manageAlliesGui.openTo(player);
            }
            case PLAYER_HEAD -> {
                if(event.isLeftClick()) {
                    NBTItem clickedNbtItem = new NBTItem(clickedItem);
                    UUID clickedGangUUID = clickedNbtItem.getUUID("gang-uuid");
                    if(clickedGangUUID == null) return;
                    Gang clickedGang = gangManager.findById(clickedGangUUID);
                    if(clickedGang == null) return;
                    GangInfoGui gangInfoGui = new GangInfoGui(player, clickedGang);
                    gangInfoGui.openTo(player);

                } else if (event.isRightClick()) {
                    NBTItem clickedNbtItem = new NBTItem(clickedItem);
                    UUID clickedGangUUID = clickedNbtItem.getUUID("gang-uuid");
                    if(clickedGangUUID == null) return;
                    Gang clickedGang = gangManager.findById(clickedGangUUID);
                    if(clickedGang == null) return;
                    if(!gang.getGangAllies().removeAllyInviteOutgoing(clickedGang)) {
                        //Todo: Change message at somepoint
                        ChatUtil.sendMessage(player, Messages.unexpectedError);
                    }
                    OutgoingAllyInvitesGui outgoingAllyInvitesGui = new OutgoingAllyInvitesGui(player, gang);
                    outgoingAllyInvitesGui.openTo(player);
                }
            }
        }
    }
}
