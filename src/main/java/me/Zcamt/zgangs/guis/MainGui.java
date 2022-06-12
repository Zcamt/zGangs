package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainGui extends GUI{

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    public MainGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBande menu"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());


        setItem(20, new ItemCreator(Material.PLAYER_HEAD).setName("&a&lDin bande").make());
        setItem(24, new ItemCreator(Material.BOOK).setName("&6&lHjælp og leaderboards").make());

        setItem(47, new ItemCreator(Material.BARRIER).setName("&cPersonlige indstillinger").make());
        setItem(51, new ItemCreator(Material.REDSTONE_TORCH).setName("&cBande indstillinger").make());
        setItem(49, new ItemCreator(Material.REDSTONE_TORCH).setName("&cLuk").make());

        //Butik
        //MOTD?

        //Personlige settings
        //Bande settings
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> player.closeInventory();
        }
    }
}
