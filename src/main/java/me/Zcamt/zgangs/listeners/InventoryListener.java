package me.Zcamt.zgangs.listeners;

import me.Zcamt.zgangs.guis.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();
        if(inventory == null) return;
        if(clickedItem == null) return;
        if(clickedItem.getType() == Material.AIR) return;
        if(inventory.getHolder() instanceof GUI gui) {
            event.setCancelled(true);
            gui.onClick(event);
        }
    }

}
