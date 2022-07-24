package me.Zcamt.zgangs.guis;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.UUID;

public class ReceivedGangInvitesGui extends GUI {

    private final Player player;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected ReceivedGangInvitesGui(Player player) {
        super(54, ChatUtil.CC("&c&lIndgående bande invitationer"));
        generateGuiBorder();
        this.player = player;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        Iterator<UUID> incomingInvitationsIterator = gangPlayer.getGangInvites().listIterator();

        for (int i = 10; i <= 43; i++) {
            if(incomingInvitationsIterator.hasNext()) {
                Gang inviteSenderGang = gangManager.findById(incomingInvitationsIterator.next());
                ItemStack gangItemStack = new ItemCreator(Material.PLAYER_HEAD)
                        .setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(inviteSenderGang.getOwnerUUID()).getName())
                        .setName("&a&l" + inviteSenderGang.getName())
                        .addLore("&7Højre-Klik her for at:",
                                "&6- &fAcceptere invitationen",
                                "",
                                "&7Venstre-Klik her for at:",
                                "&6- &fAfvise invitationen")
                        .make();

                NBTItem gangNbtItem = new NBTItem(gangItemStack, true);
                gangNbtItem.setUUID("gang-uuid", inviteSenderGang.getUUID());
                setItem(i, gangNbtItem.getItem());
            }
        }

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        int clickedSlot = event.getSlot();
        if(clickedItem == null) return;
        if(gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                NoGangGui noGangGui = new NoGangGui(player);
                noGangGui.openTo(player);
            }
            case PLAYER_HEAD -> {
                if(event.isLeftClick()) {
                    NBTItem clickedNbtItem = new NBTItem(clickedItem);
                    UUID clickedGangUUID = clickedNbtItem.getUUID("gang-uuid");
                    if (clickedGangUUID == null) return;
                    Gang clickedGang = gangManager.findById(clickedGangUUID);
                    if (clickedGang == null) return;
                    if(!clickedGang.getGangMembers().addGangPlayerToGang(gangPlayer)) {
                        ChatUtil.sendMessage(player, Messages.unexpectedError);
                    }

                } else if (event.isRightClick()) {
                    NBTItem clickedNbtItem = new NBTItem(clickedItem);
                    UUID clickedGangUUID = clickedNbtItem.getUUID("gang-uuid");
                    if (clickedGangUUID == null) return;
                    Gang clickedGang = gangManager.findById(clickedGangUUID);
                    if (clickedGang == null) return;
                    clickedGang.getGangMembers().removePlayerFromInvites(gangPlayer);
                }
            }
        }
    }
}
