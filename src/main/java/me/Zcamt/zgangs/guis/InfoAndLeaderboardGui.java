package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.level.GangLevel;
import me.Zcamt.zgangs.objects.gang.level.GangLevelManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.objects.leaderboard.LeaderboardType;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InfoAndLeaderboardGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final GangLevelManager gangLevelManager = ZGangs.getGangLevelManager();

    protected InfoAndLeaderboardGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lInfo og leaderboards"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        setItem(20, new ItemCreator(Material.PAPER)
                .setName("&a&lKommandoer i systemet")
                .addLore("&6- &f/bandechat",
                        "&6- &f/allychat",
                        "&6- &fFlere kommandoer via /bk")
                .make());
        setItem(22, new ItemCreator(Material.GOLDEN_SWORD)
                .setName("&6&lLeaderboards")
                .addLore("&7Klik her for at se",
                        "&7følgende leaderboards:",
                        "&6- &fFlest drab",
                        "&6- &fFlest vagt-drab",
                        "&6- &fFlest officer+-drab",
                        "&6- &fFlest penge",
                        "&6- &fFlest døde",
                        "&6- &fHøjest level")
                .make());
        GangLevel nextGangLevel = gangLevelManager.getGangLevelFromInt(gang.getLevel()+1);
        setItem(24, new ItemCreator(Material.NETHER_STAR)
                .setName("&a&lHvad får vi i næste bande level?")
                .addLore(nextGangLevel.getNewFeaturesForLevel())
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
                MainGui mainGui = new MainGui(player, gang);
                mainGui.openTo(player);
            }
            case GOLDEN_SWORD -> {
                LeaderboardGui leaderboardGui = new LeaderboardGui(player, gang, LeaderboardType.KILLS);
                leaderboardGui.openTo(player);
            }
        }
    }
}
