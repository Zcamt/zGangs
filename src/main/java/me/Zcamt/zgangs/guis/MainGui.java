package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.gangstats.GangStat;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import me.Zcamt.zgangs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainGui extends GUI {
    private final Player player;
    private final @Nullable Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    public MainGui(Player player) {
        super(54, ChatUtil.CC("&c&lBande menu"));
        generateGuiBorder();
        this.player = player;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());
        this.gang = gangManager.findById(this.gangPlayer.getGangUUID());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cLuk").make());

        if (gangPlayer.isInGang()) {
            setItem(22, new ItemCreator(Material.PLAYER_HEAD)
                    .setName("&a&lDin bande").addLore(
                            "&c&lNavn: &f" + gang.getName(),
                            "&c&lLevel: &f" + gang.getLevel(),
                            "&c&lEjer: &f" + Bukkit.getOfflinePlayer(gang.getOwnerUUID()).getName(),
                            "&c&lBank: &f" + gang.getBank(),
                            "&c&lDrab: &f" + gang.getGangStats().getStatAmount(GangStat.KILLS),
                            "&c&lDøde: &f" + gang.getGangStats().getStatAmount(GangStat.DEATHS),
                            "&c&lVagt drab: &f" +
                                    (gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_A)
                                    + gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_B)
                                    + gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_C)),
                            "&c&lOfficer+ drab: &f" + gang.getGangStats().getStatAmount(GangStat.OFFICER_PLUS_KILLS),
                            "&c&lVagt drab i A: &f" + gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_A),
                            "&c&lVagt drab i B: &f" + gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_B),
                            "&c&lVagt drab i C: &f" + gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_C),
                            "&c&lBande oprettet: &f" + Utils.formatDateFromEpochMilli(gang.getCreationDateMillis())
                    ).make());

            List<GangPlayer> gangMembers = new ArrayList<>();
            gang.getGangMembers().getMemberList().forEach(uuid -> gangMembers.add(gangPlayerManager.findById(uuid)));
            gangMembers.sort(Comparator.comparing(GangPlayer::getGangRank));
            List<String> memberLore = new ArrayList<>();
            gangMembers.forEach(gangMember ->
                    memberLore.add("&c&l" + gangMember.getGangRank().getName()
                                    + (gangMember.getOfflinePlayer().isOnline() ? " &a●" : " &7●")
                                    + " &f" + gangMember.getOfflinePlayer().getName() )
            );
            setItem(24, new ItemCreator(Material.PAPER)
                    .setName("&a&lMedlemmer").addLore(memberLore).make());
        } else {
            setItem(22, new ItemCreator(Material.PLAYER_HEAD)
                    .setName("&a&lUventet fejl...").addLore(
                            "&cDet lader til at du er blevet vist en forkert menu.",
                            "&cKontakt venligst en admin!"
                    ).make());
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> player.closeInventory();
        }
    }
}
