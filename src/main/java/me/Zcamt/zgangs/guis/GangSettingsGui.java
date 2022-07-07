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

public class GangSettingsGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    public GangSettingsGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBande indstillinger"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());

        //Rename
        setItem(10, new ItemCreator(Material.NAME_TAG).setName("&aSkift navn").make());

        //Manage members
        setItem(12, new ItemCreator(Material.PLAYER_HEAD).setName("&aHåndter medlemmer").make());

        //Manage MOTD
        setItem(14, new ItemCreator(Material.BOOK).setName("&aHåndter MOTD").make());

        //Manage allies
        setItem(16, new ItemCreator(Material.GREEN_BANNER).setName("&aHåndter allierede").make());

        //Bank-deposit
        setItem(28, new ItemCreator(Material.GOLD_NUGGET).setName("&aIndsæt penge").make());

        //Manage gang-rank permissions
        setItem(30, new ItemCreator(Material.GOLDEN_SWORD).setName("&aIndsæt penge").make());

        //Delete gang
        setItem(32, new ItemCreator(Material.RED_DYE).setName("&aSlet bande").make());

        //Manage rivals
        setItem(34, new ItemCreator(Material.RED_BANNER).setName("&aHåndter rivaler").make());
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
        }
    }

}
