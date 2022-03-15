package me.Zcamt.zgangs.guis;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MainGui extends GUI2{
    private final UUID uuid;

    public MainGui(UUID uuid) {
        super(9, "Testing");
        setItem(0, new ItemStack(Material.STRING));

        this.uuid = uuid;
    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }
}
