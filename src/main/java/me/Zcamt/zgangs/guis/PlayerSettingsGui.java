package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSettingsGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected PlayerSettingsGui(Player player, Gang playerGang) {
        super(54, "&a&lPersonlige indstillinger");
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        //Notifikations menu
        setItem(21, new ItemCreator(Material.PAPER).setName("&a&lNotifikationer")
                .addLore("&7Klik her for at:",
                        "&6- &fÆndre hvornår du får notifikationer")
                .make());

        //Forlad bande
        setItem(23, new ItemCreator(Material.RED_DYE).setName("&4&lForlad bande")
                .addLore("&7Klik her for at:",
                        "&6- &fForlade din bande")
                .make());
        
        setItem(49, new ItemCreator(Material.REDSTONE_TORCH).setName("&cTilbage").make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                player.closeInventory();
                MainGui mainGui = new MainGui(player, gang);
                mainGui.openTo(player);
            }
            case RED_DYE -> {
                if(gangPlayer.getGangRank().compare(GangRank.OWNER) >= 0) {
                    ChatUtil.sendMessage(player, Messages.cantLeaveAsOwner);
                    break;
                }

                if(gang.getGangMembers().removeGangPlayerFromGang(gangPlayer)) {
                    gang.sendMessageToOnlineMembers(Messages.playerLeftGang(player.getName()));
                    ChatUtil.sendMessage(player, Config.prefix + " &a&lDu har forladt din bande");
                } else {
                    ChatUtil.sendMessage(player, Messages.unexpectedError);
                }
            }
            case PAPER -> {
                player.closeInventory();
                PlayerNotificationsGui playerNotificationsGui = new PlayerNotificationsGui(player, gang);
                playerNotificationsGui.openTo(player);
            }
        }
    }
}

class PlayerNotificationsGui extends GUI {
    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected PlayerNotificationsGui(Player player, Gang playerGang) {
        super(54, "&a&lNotifikations indstillinger");
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        //Gang chat 12
        boolean gangChatEnabled = gangPlayer.getGangPlayerSettings().isReceiveGangChat();
        setItem(12, new ItemCreator(gangChatEnabled ? Material.GREEN_BANNER : Material.RED_DYE)
                .setName("&a&lBandechat")
                .addLore("&7Klik her for at:",
                        "&6- &fDe(aktivere) beskeder fra bandechatten")
                .make());
        //Ally chat 14
        boolean allyChatEnabled = gangPlayer.getGangPlayerSettings().isReceiveAllyChat();
        setItem(14, new ItemCreator(allyChatEnabled ? Material.GREEN_BANNER : Material.RED_DYE)
                .setName("&a&lAllychat")
                .addLore("&7Klik her for at:",
                        "&6- &fDe(aktivere) beskeder fra allychatten")
                .make());

        //Member connect 21
        boolean memberConnectEnabled = gangPlayer.getGangPlayerSettings().isReceiveMemberConnectNotification();
        setItem(21, new ItemCreator(memberConnectEnabled ? Material.GREEN_BANNER : Material.RED_DYE)
                .setName("&a&lMedlem online")
                .addLore("&7Klik her for at:",
                        "&6- &fDe(aktivere) online-notifikationer for medlemmer")
                .make());
        //Member disconnect 23
        boolean memberDisconnectEnabled = gangPlayer.getGangPlayerSettings().isReceiveMemberDisconnectNotification();
        setItem(23, new ItemCreator(memberDisconnectEnabled ? Material.GREEN_BANNER : Material.RED_DYE)
                .setName("&a&lMedlem offline")
                .addLore("&7Klik her for at:",
                        "&6- &fDe(aktivere) offline-notifikationer for medlemmer")
                .make());


        //Ally connect 30
        boolean allyConnectEnabled = gangPlayer.getGangPlayerSettings().isReceiveAllyConnectNotification();
        setItem(30, new ItemCreator(allyConnectEnabled ? Material.GREEN_BANNER : Material.RED_DYE)
                .setName("&a&lAllieret online")
                .addLore("&7Klik her for at:",
                        "&6- &fDe(aktivere) online-notifikationer for allierede")
                .make());
        //Ally disconnect 32
        boolean allyDisconnectEnabled = gangPlayer.getGangPlayerSettings().isReceiveAllyDisconnectNotification();
        setItem(32, new ItemCreator(allyDisconnectEnabled ? Material.GREEN_BANNER : Material.RED_DYE)
                .setName("&a&lAllieret offline")
                .addLore("&7Klik her for at:",
                        "&6- &fDe(aktivere) offline-notifikationer for allierede")
                .make());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        int clickedSlot = event.getSlot();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                player.closeInventory();
                PlayerSettingsGui playerSettingsGui = new PlayerSettingsGui(player, gang);
                playerSettingsGui.openTo(player);
            }
        }
        switch (clickedSlot) {
            case 12 -> {
                gangPlayer.getGangPlayerSettings().toggleReceiveGangChat();
                player.closeInventory();
                this.openTo(player);
            }
            case 14 -> {
                gangPlayer.getGangPlayerSettings().toggleReceiveAllyChat();
                player.closeInventory();
                this.openTo(player);
            }
            case 21 -> {
                gangPlayer.getGangPlayerSettings().toggleReceiveMemberConnectNotification();
                player.closeInventory();
                this.openTo(player);
            }
            case 23 -> {
                gangPlayer.getGangPlayerSettings().toggleReceiveMemberDisconnectNotification();
                player.closeInventory();
                this.openTo(player);
            }
            case 30 -> {
                gangPlayer.getGangPlayerSettings().toggleReceiveAllyConnectNotification();
                player.closeInventory();
                this.openTo(player);
            }
            case 32 -> {
                gangPlayer.getGangPlayerSettings().toggleReceiveAllyDisconnectNotification();
                player.closeInventory();
                this.openTo(player);
            }
        }
    }
}
