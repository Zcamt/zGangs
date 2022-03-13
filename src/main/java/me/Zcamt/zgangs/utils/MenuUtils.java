package me.Zcamt.zgangs.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class MenuUtils {

    public static HashMap<Integer, ItemStack> generateBorder(HashMap<Integer, ItemStack> map){
        ItemStack filler = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).setName("").make();
        for (int i = 0; i < 54; i++) {
            map.put(i, filler);
        }

        ItemStack air = new ItemCreator(Material.AIR).make();
        for (int i = 10; i < 35; i++) {
            if (i != 17 && i != 26 && i != 18 && i != 27) {
                map.put(i, air);
            }
        }
        return map;
    }

}
