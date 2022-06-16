package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.motd.GangMotd;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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


        setItem(20, new ItemCreator(Material.PLAYER_HEAD)
                .setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(gang.getOwnerUUID()).getName())
                .setName("&a&lDin bande")
                .addLore("Klik her for at:",
                        "&6- &fSe information om din bande",
                        "&6- &fSe krav til næste bande-level",
                        "&6- &fSe medlemmerne i din bande")
                .make());
        setItem(22, new ItemCreator(Material.BOOK).setName("&a&lMOTD").addLore(gang.getGangMotd().getFullMotd()).make());
        setItem(24, new ItemCreator(Material.GOLDEN_SWORD).setName("&6&lInfo og leaderboards")
                .addLore("Klik her for at:",
                        "&6- &fSe bande leaderboards",
                        "&6- &fSe information om din bandes næste level",
                        "&6- &fSe hvilke kommandoer der findes i systemet")
                .make());

        setItem(47, new ItemCreator(Material.BARRIER).setName("&cPersonlige indstillinger").make());
        setItem(51, new ItemCreator(Material.REDSTONE_TORCH).setName("&cBande indstillinger").make());
        setItem(49, new ItemCreator(Material.REDSTONE_TORCH).setName("&cLuk").make());

        //Butik

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
