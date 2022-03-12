package me.Zcamt.zgangs.menu;

import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class MainGUI extends GUI {
    private Inventory inventory;
    private final String title = "Main Menu";
    private final int size = 54;
    private final UUID uuid;

    public MainGUI(UUID uuid) {
        this.uuid = uuid;
        inventory = super.createGUI(title, size, items());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        player.sendMessage("you clicked the gui with the name " + title);
    }

    @Override
    public HashMap<Integer, ItemStack> items() {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        items.put(1, new ItemCreator(Material.DIAMOND_PICKAXE).setName("uuid: " + uuid.toString()).make());
        return items;
    }

    @Override
    public void updateInventory() {
        inventory = super.createGUI(title, size, items());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }


}
