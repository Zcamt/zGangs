package me.Zcamt.zgangs.confirmaction;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.guis.GUI;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ConfirmActionGui extends GUI {

    private final Player player;
    private final ConfirmActionManager confirmActionManager = ZGangs.getConfirmActionManager();

    protected ConfirmActionGui(Player player) {
        super(54, "&aBekræft handling");
        generateGuiBorder();
        this.player = player;

        ItemStack yesButton = new ItemCreator(Material.GREEN_STAINED_GLASS).setName("&a&lBekræft").make();
        ItemStack noButton = new ItemCreator(Material.RED_STAINED_GLASS).setName("&a&lAfkræft").make();
        ItemStack filler = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).setName("").make();
        List<Integer> yesList = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
        List<Integer> noList = Arrays.asList(14, 15, 16, 23, 24, 25, 32, 33, 34);

        for (int i = 10; i < 35; i++) {
            if(yesList.contains(i)) {
                setItem(i, yesButton);
            } else if(noList.contains(i)) {
                setItem(i, noButton);
            } else {
                setItem(i, filler);
            }
        }

        setItem(22, new ItemCreator(Material.BOOK).setName("&6Handling du bekræfter")
                .addLore(confirmActionManager.getConfirmationFromPlayer(player).getActionDescription())
                .make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        switch (clickedItem.getType()) {
            case GREEN_STAINED_GLASS -> {
                confirmActionManager.getConfirmationFromPlayer(player).run();
                confirmActionManager.removeFromConfirmationMap(player);
                player.closeInventory();
            }
            case RED_STAINED_GLASS -> {
                confirmActionManager.removeFromConfirmationMap(player);
                player.closeInventory();
            }
        }
    }
}
