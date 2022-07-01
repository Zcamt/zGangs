package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.objects.leaderboard.GangLeaderboardEntry;
import me.Zcamt.zgangs.objects.leaderboard.Leaderboard;
import me.Zcamt.zgangs.objects.leaderboard.LeaderboardManager;
import me.Zcamt.zgangs.objects.leaderboard.LeaderboardType;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;

public class LeaderboardGui extends GUI {

    private final LeaderboardType leaderboardType;
    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final LeaderboardManager leaderboardManager = ZGangs.getLeaderboardManager();

    protected LeaderboardGui(Player player, Gang playerGang, LeaderboardType leaderboardType) {
        super(54, ChatUtil.CC("&c&lLeaderboards"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.leaderboardType = leaderboardType;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());


        //Todo: Could add a "last updated" section in the lore of each
        // or a main button between the 2 pairs of 3 that'll display when they all where updated last
        setItem(37, new ItemCreator(Material.IRON_SWORD)
                .setName("&a&lDrab" + (leaderboardType == LeaderboardType.KILLS ? " &a(aktiv)" : ""))
                .addLore("&7Klik her for at:",
                        "&6- &fSe de bander med flest drab").make());

        setItem(38, new ItemCreator(Material.GOLDEN_HELMET)
                .setName("&a&lVagt-drab" + (leaderboardType == LeaderboardType.GUARD_KILLS ? " &a(aktiv)" : ""))
                .addLore("&7Klik her for at:",
                        "&6- &fSe de bander med flest vagt-drab").make());

        setItem(39, new ItemCreator(Material.CHAINMAIL_HELMET)
                .setName("&a&lOfficer+-drab" + (leaderboardType == LeaderboardType.OFFICER_PLUS_KILLS ? " &a(aktiv)" : ""))
                .addLore("&7Klik her for at:",
                        "&6- &fSe de bander med flest officer+-drab").make());

        setItem(41, new ItemCreator(Material.SKELETON_SKULL)
                .setName("&a&lDøde" + (leaderboardType == LeaderboardType.DEATHS ? " &a(aktiv)" : ""))
                .addLore("&7Klik her for at:",
                        "&6- &fSe de bander med flest døde").make());

        setItem(42, new ItemCreator(Material.GOLD_INGOT)
                .setName("&a&lBank" + (leaderboardType == LeaderboardType.BANK ? " &a(aktiv)" : ""))
                .addLore("&7Klik her for at:",
                        "&6- &fSe de bander med flest penge").make());

        setItem(43, new ItemCreator(Material.NETHER_STAR)
                .setName("&a&lLevel" + (leaderboardType == LeaderboardType.LEVEL ? " &a(aktiv)" : ""))
                .addLore("&7Klik her for at:",
                        "&6- &fSe de bander med højest level").make());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());

        loadLeaderboardType(leaderboardType);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                InfoAndLeaderboardGui infoAndLeaderboardGui = new InfoAndLeaderboardGui(player, gang);
                infoAndLeaderboardGui.openTo(player);
            }
            case IRON_SWORD -> {
                LeaderboardGui leaderboardGui = new LeaderboardGui(player, gang, LeaderboardType.KILLS);
                leaderboardGui.openTo(player);
            }
            case GOLDEN_HELMET -> {
                LeaderboardGui leaderboardGui = new LeaderboardGui(player, gang, LeaderboardType.GUARD_KILLS);
                leaderboardGui.openTo(player);
            }
            case CHAINMAIL_HELMET -> {
                LeaderboardGui leaderboardGui = new LeaderboardGui(player, gang, LeaderboardType.OFFICER_PLUS_KILLS);
                leaderboardGui.openTo(player);
            }
            case SKELETON_SKULL -> {
                LeaderboardGui leaderboardGui = new LeaderboardGui(player, gang, LeaderboardType.DEATHS);
                leaderboardGui.openTo(player);
            }
            case GOLD_INGOT -> {
                LeaderboardGui leaderboardGui = new LeaderboardGui(player, gang, LeaderboardType.BANK);
                leaderboardGui.openTo(player);
            }
            case NETHER_STAR -> {
                LeaderboardGui leaderboardGui = new LeaderboardGui(player, gang, LeaderboardType.LEVEL);
                leaderboardGui.openTo(player);
            }
        }
    }

    private void loadLeaderboardType(LeaderboardType type){
        Leaderboard leaderboard = leaderboardManager.getLeaderboardFromType(type);
        List<GangLeaderboardEntry> leaderboardEntries = leaderboard.getLeaderboard();
        Iterator<GangLeaderboardEntry> entryIterator = leaderboardEntries.listIterator();
        int place = 1;
        for (int i = 10; i <= 43; i++) {
            if(entryIterator.hasNext()) {
                String placeColor;
                switch (place) {
                    default -> placeColor = "&7";
                    case 1 -> placeColor = "&e";
                    case 2 -> placeColor = "&3";
                    case 3 -> placeColor = "&6";
                }
                GangLeaderboardEntry entry = entryIterator.next();
                Gang entryGang = gangManager.findById(entry.getGangUuid());
                setItem(i, new ItemCreator(Material.PLAYER_HEAD)
                        .setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(entryGang.getOwnerUUID()).getName())
                        .setName(placeColor+"&l#"+ place++ + " " + entryGang.getName())
                        .addLore(
                                (type == LeaderboardType.KILLS ? "&a&l" : "&7") + "Drab: " + entryGang.getGangStats().getKills(),
                                (type == LeaderboardType.GUARD_KILLS ? "&a&l" : "&7") +"&7Vagt-drab: " + entryGang.getGangStats().getAllGuardKills(),
                                (type == LeaderboardType.OFFICER_PLUS_KILLS ? "&a&l" : "&7") +"&7Officer+-drab: " + entryGang.getGangStats().getOfficer_plus_kills(),
                                (type == LeaderboardType.DEATHS ? "&a&l" : "&7") +"&7Døde: " + entryGang.getGangStats().getDeaths(),
                                (type == LeaderboardType.BANK ? "&a&l" : "&7") +"&7Bank: " + entryGang.getBank(),
                                (type == LeaderboardType.LEVEL ? "&a&l" : "&7") +"&7Level: " + entryGang.getLevel())
                        .make());
            } else {
                break;
            }
        }
    }
}
