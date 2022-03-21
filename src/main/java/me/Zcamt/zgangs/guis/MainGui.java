package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class MainGui extends GUI2{
    private final UUID uuid;

    public MainGui(UUID uuid) {
        super(9, "Testing");
        generateGuiBorder();
        setItem(49, new ItemCreator(Material.BARRIER).setName("&cLuk").make());
        this.uuid = uuid;
    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }
}
