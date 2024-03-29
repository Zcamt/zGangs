package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.chatinput.ChatInputManager;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.objects.leaderboard.LeaderboardType;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class NoGangGui extends GUI {
    private final Player player;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final ChatInputManager chatInputManager = ZGangs.getChatInputManager();
    private final Economy economy = ZGangs.getEconomy();

    public NoGangGui(Player player) {
        super(54, ChatUtil.CC("&c&lIngen bande"));
        generateGuiBorder();
        this.player = player;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        setItem(20, new ItemCreator(Material.GOLDEN_SWORD)
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

        setItem(22, new ItemCreator(Material.NETHER_STAR)
                .setName("&a&lOpret en bande").addLore(
                        "&cDu har ingen bande...",
                        "&cKlik her for at oprette en!",
                        "&cPris: &f" + Config.createGangCost
                ).make());

        setItem(24, new ItemCreator(Material.PAPER)
                .setName("&a&lBande invitationer").addLore(
                        "&cKlik her for at se",
                        "&cHvilke bander du er blevet inviteret til",
                        "&cAntal invitationer: &f" + gangPlayer.getGangInvites().size()
                ).make());
        setItem(49, new ItemCreator(Material.BARRIER).setName("&cLuk").make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> player.closeInventory();
            case NETHER_STAR -> {
                chatInputManager.newStringInput(player, name -> {
                    if(!economy.has(player, Config.createGangCost)) {
                        ChatUtil.sendMessage(player, Messages.notEnoughMoney);
                        return;
                    }
                    EconomyResponse response = economy.withdrawPlayer(player, Config.createGangCost);
                    if(!response.transactionSuccess()) {
                        ChatUtil.sendMessage(player, response.errorMessage);
                        return;
                    }
                    Gang gang = gangManager.createNewGang(name, gangPlayer);
                    ChatUtil.sendMessage(player, Config.prefix + " &a&lDu har nu oprettet &c&l" + gang.getName() + " &a&l- Tillykke med din nye bande!");
                });
                ChatUtil.sendMessage(player, Config.prefix + " &a&lSkriv navnet på din nye bande i chatten! " +
                        "- Hvis du ønsker at afbryde processen tast '&c&l-afbryd&a&l'");
                player.closeInventory();
            }
            case GOLDEN_SWORD -> {
                LeaderboardGui leaderboardGui = new LeaderboardGui(player, LeaderboardType.KILLS);
                leaderboardGui.openTo(player);
            }
            case PAPER -> {
                ReceivedGangInvitesGui receivedGangInvitesGui = new ReceivedGangInvitesGui(player);
                receivedGangInvitesGui.openTo(player);
            }
        }
    }
}
