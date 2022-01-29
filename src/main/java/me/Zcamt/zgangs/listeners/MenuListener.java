package me.Zcamt.zgangs.listeners;

import me.Zcamt.zgangs.guis.GUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory inventory = event.getInventory();
        if(!(inventory.getHolder() instanceof GUI)) return;
        if(event.getClickedInventory() == null) return;
        if(event.getCurrentItem() == null) return;

        event.setCancelled(true);
        ((GUI) inventory.getHolder()).onClick(event);
    }

}
