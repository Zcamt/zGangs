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

public class ManageRivalsGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected ManageRivalsGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBandens rivaler"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());


        setItem(20, new ItemCreator(Material.RED_BANNER).setName("&aNuværende rivaler")
                .addLore("&7Klik her for at:",
                        "&6- &fSe hvem I har markeret som rival")
                .make());

        setItem(24, new ItemCreator(Material.SKELETON_SKULL).setName("&aRivaler mod banden")
                .addLore("&7Klik her for at:",
                        "&6- &fSe hvem der har markeret jer som rival")
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
            case RED_BANNER -> {
                CurrentRivalsGui currentRivalsGui = new CurrentRivalsGui(player, gang);
                currentRivalsGui.openTo(player);
            }
            case SKELETON_SKULL -> {
                CurrentRivalsAgainstGui currentRivalsAgainstGui = new CurrentRivalsAgainstGui(player, gang);
                currentRivalsAgainstGui.openTo(player);
            }
        }
    }
}

class CurrentRivalsGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected CurrentRivalsGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lNuværende allierede"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        Iterator<UUID> rivalGangsIterator = gang.getGangRivals().getRivalGangs().listIterator();

        for (int i = 10; i <= 43; i++) {
            if (rivalGangsIterator.hasNext()) {
                Gang rivalGang = gangManager.findById(rivalGangsIterator.next());
                ItemStack rivalItemStack = new ItemCreator(Material.PLAYER_HEAD)
                        .setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(rivalGang.getOwnerUUID()).getName())
                        .setName("&a&l" + rivalGang.getName())
                        .addLore("&7Højre-Klik her for at:",
                                "&6- &fSe information om banden",
                                "",
                                "&7Venstre-Klik her for at:",
                                "&6- &fFjerne banden som rival")
                        .make();

                NBTItem rivalNbtItem = new NBTItem(rivalItemStack, true);
                rivalNbtItem.setUUID("gang-uuid", rivalGang.getUUID());
                setItem(i, rivalNbtItem.getItem());
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
                ManageRivalsGui manageRivalsGui = new ManageRivalsGui(player, gang);
                manageRivalsGui.openTo(player);
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
                    if(gang.getGangRivals().removeRival(clickedGang)) {
                        gang.sendMessageToOnlineMembers(Messages.noLongerRival(clickedGang.getName()));
                        clickedGang.sendMessageToOnlineMembers(Messages.noLongerRivalAgainst(gang.getName()));

                        CurrentRivalsGui currentRivalsGui = new CurrentRivalsGui(player, gang);
                        currentRivalsGui.openTo(player);
                    }
                }
            }
        }
    }
}

class CurrentRivalsAgainstGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected CurrentRivalsAgainstGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lNuværende allierede"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        Iterator<UUID> rivalGangsAgainstIterator = gang.getGangRivals().getRivalGangsAgainst().listIterator();

        for (int i = 10; i <= 43; i++) {
            if (rivalGangsAgainstIterator.hasNext()) {
                Gang rivalGangAgainst = gangManager.findById(rivalGangsAgainstIterator.next());
                ItemStack rivalAgainstItemStack = new ItemCreator(Material.PLAYER_HEAD)
                        .setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(rivalGangAgainst.getOwnerUUID()).getName())
                        .setName("&a&l" + rivalGangAgainst.getName())
                        .addLore("&7Højre-Klik her for at:",
                                "&6- &fSe information om banden")
                        .make();

                NBTItem rivalNbtItem = new NBTItem(rivalAgainstItemStack, true);
                rivalNbtItem.setUUID("gang-uuid", rivalGangAgainst.getUUID());
                setItem(i, rivalNbtItem.getItem());
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
                ManageRivalsGui manageRivalsGui = new ManageRivalsGui(player, gang);
                manageRivalsGui.openTo(player);
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
                }
            }
        }
    }
}