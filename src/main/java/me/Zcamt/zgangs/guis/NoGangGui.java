package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.chatinput.ChatInputManager;
import me.Zcamt.zgangs.config.Config;
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

public class NoGangGui extends GUI {
    private final Player player;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final ChatInputManager chatInputManager = ZGangs.getChatInputManager();

    public NoGangGui(Player player) {
        super(54, ChatUtil.CC("&c&lIngen bande"));
        generateGuiBorder();
        this.player = player;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

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
                    Gang gang = gangManager.createNewGang(name, gangPlayer);
                    ChatUtil.sendMessage(player, Config.prefix + " &a&lDu har nu oprettet &c&l" + gang.getName() + " &a&l- Tillykke med din nye bande!");
                });
                ChatUtil.sendMessage(player, Config.prefix + " &a&lSkriv navnet på din nye bande i chatten! " +
                        "- Hvis du ønsker at afbryde processen tast '&c&l-afbryd&a&l'");
                player.closeInventory();
            }
        }
    }
}
