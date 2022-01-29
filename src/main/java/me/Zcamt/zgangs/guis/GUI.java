package me.Zcamt.zgangs.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class GUI implements InventoryHolder {

    protected Inventory createGUI(String title, int size, HashMap<Integer, ItemStack> items){
        Inventory inventory = Bukkit.createInventory(this, size, title);
        items.forEach(inventory::setItem);
        return inventory;
    }

    public abstract void onClick(InventoryClickEvent event);

    public abstract HashMap<Integer, ItemStack> items();

    public abstract void updateInventory();

}
