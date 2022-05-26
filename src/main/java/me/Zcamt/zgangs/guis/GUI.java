package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class GUI implements InventoryHolder {
    private Inventory inventory;

    protected GUI(int size, String name) {
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

    protected void generateGuiBorder(){
        ItemStack filler = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).setName("").make();
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, filler);
        }

        ItemStack air = new ItemCreator(Material.AIR).make();
        for (int i = 10; i < 35; i++) {
            if (i != 17 && i != 26 && i != 18 && i != 27) {
                inventory.setItem(i, air);
            }
        }
    }

}
