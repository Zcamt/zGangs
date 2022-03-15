package me.Zcamt.zgangs.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class GUI2 implements InventoryHolder {
    private Inventory inventory;

    protected GUI2(int size, String name) {
       this.inventory = Bukkit.createInventory(this, size, name);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setItem(int slot, ItemStack stack){
        inventory.setItem(slot, stack);
    }

    public void openTo(Player player){
        player.openInventory(inventory);
    }

    public abstract void onClick(InventoryClickEvent event);

}
