package me.Zcamt.zgangs.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class NpcListener implements Listener {

    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    @EventHandler
    public void onNpcClick(NPCRightClickEvent event) {
        NPC clickedNPC = event.getNPC();
        if(!clickedNPC.getName().contains("Mafia Boss")) return;

        Player player = event.getClicker();
        GangPlayer gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        if(gangPlayer == null || !gangPlayer.isInGang()) return;
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if(heldItem.getType().equals(Material.AIR)) return;
        Gang gang = gangManager.findById(gangPlayer.getGangUUID());
        int heldItemAmount = heldItem.getAmount();

        switch (heldItem.getType()){
            case BREAD -> {
                int currentBreadDelivered = gang.getGangItemDelivery().getBreadDelivered();
                player.getInventory().setItemInMainHand(null);
                gang.getGangItemDelivery().setBreadDelivered(currentBreadDelivered+heldItemAmount);
            }
            case BLAZE_ROD -> {
                NBTItem heldNBTItem = new NBTItem(heldItem);
                if(heldNBTItem.getBoolean("is-gang-cig")) {
                    int currentCigsDelivered = gang.getGangItemDelivery().getCigsDelivered();
                    player.getInventory().setItemInMainHand(null);
                    gang.getGangItemDelivery().setBreadDelivered(currentCigsDelivered + heldItemAmount);
                }
            }
        }
    }

}
