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

    private int eastereggCounter;
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
                .addLore("&7Klik her for at:",
                        "&6- &fSe dine bandes stats",
                        "&6- &fSe medlemmerne i din bande",
                        "&6- &fSe krav til næste bande-level",
                        "&6- &fOg meget mere...")
                .make());
        setItem(22, new ItemCreator(Material.BOOK).setName("&a&lMOTD").addLore(gang.getGangMotd().getFullMotd()).make());
        setItem(24, new ItemCreator(Material.GOLDEN_SWORD).setName("&6&lInfo og leaderboards")
                .addLore("&7Klik her for at:",
                        "&6- &fSe bande leaderboards",
                        "&6- &fSe information om din bandes næste level",
                        "&6- &fSe hvilke kommandoer der findes i systemet")
                .make());

        setItem(47, new ItemCreator(Material.REDSTONE_TORCH).setName("&cPersonlige indstillinger")
                .addLore("&7Klik her for at:",
                        "&6- &fForlade din bande",
                        "&6- &f(De)aktivere beskeder fra systemet")
                .make());
        setItem(51, new ItemCreator(Material.REDSTONE_TORCH).setName("&cBande indstillinger")
                .addLore("&7Klik her for at:",
                        "&6- &fÆndre adgang for bandens roller",
                        "&6- &fSætte bandens MOTD",
                        "&6- &fHåndtere bandens medlemmer",
                        "&6- &fOg meget mere...")
                .make());
        setItem(49, new ItemCreator(Material.BARRIER).setName("&cLuk").make());

        //Butik

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        int clickedSlot = event.getSlot();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        if(clickedSlot == 53 && eastereggCounter < 10) {
            if(++eastereggCounter == 10) {
                ChatUtil.sendMessage(player, "Du har trykket 10 gange på den knap, tilykke!");
                ChatUtil.sendMessage(player, "Vidste du forresten...");
                ChatUtil.sendMessage(player, "At de fleste ænder ikke går i kirke?");
            }
        }
        switch (clickedItem.getType()) {
            case BARRIER -> player.closeInventory();
            case PLAYER_HEAD -> {
                GangInfoGui gangInfoGui = new GangInfoGui(player, gang);
                gangInfoGui.openTo(player);
            }
            case REDSTONE_TORCH -> {
                if(clickedItem.getItemMeta() == null) return;
                String clickedItemName = clickedItem.getItemMeta().getDisplayName();
                if(clickedItemName.contains("Personlige indstillinger")) {
                    PlayerSettingsGui playerSettingsGui = new PlayerSettingsGui(player, gang);
                    playerSettingsGui.openTo(player);
                } else if(clickedItemName.contains("Bande indstillinger")) {
                    GangSettingsGui gangSettingsGui = new GangSettingsGui(player, gang);
                    gangSettingsGui.openTo(player);
                }
            }
            case GOLDEN_SWORD -> {
                InfoAndLeaderboardGui infoAndLeaderboardGui = new InfoAndLeaderboardGui(player, gang);
                infoAndLeaderboardGui.openTo(player);
            }
        }
    }
}
