package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
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
        //Forlad bande
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
            case PAPER -> {
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
        super(54, "&a&lBesked indstillinger");
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        //Gang chat
        //Ally chat
        //Member connect
        //Member disconnect
        //Ally connect
        //Ally disconnect
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
                PlayerSettingsGui playerSettingsGui = new PlayerSettingsGui(player, gang);
                playerSettingsGui.openTo(player);
            }
        }
    }
}
