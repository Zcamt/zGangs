package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.level.GangLevel;
import me.Zcamt.zgangs.objects.gang.level.GangLevelManager;
import me.Zcamt.zgangs.objects.gang.level.GangLevelRequirement;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GangInfoGui extends GUI {
    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final GangLevelManager gangLevelManager = ZGangs.getGangLevelManager();

    public GangInfoGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lDin bande " + playerGang.getName()));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());

        if (gangPlayer.isInGang()) {
            //Gang info 13
            setItem(13, new ItemCreator(Material.BOOK)
                    .setName("&a&lDin bande").addLore(
                            "&c&lNavn: &f" + gang.getName(),
                            "&c&lLevel: &f" + gang.getLevel(),
                            "&c&lEjer: &f" + Bukkit.getOfflinePlayer(gang.getOwnerUUID()).getName(),
                            "&c&lBank: &f" + gang.getBank(),
                            "&c&lDrab: &f" + gang.getGangStats().getKills(),
                            "&c&lDøde: &f" + gang.getGangStats().getDeaths(),
                            "&c&lVagt drab: &f" + gang.getGangStats().getAllGuardKills(),
                            "&c&lOfficer+ drab: &f" + gang.getGangStats().getOfficer_plus_kills(),
                            "&c&lVagt drab i A: &f" + gang.getGangStats().getGuard_kills_in_a(),
                            "&c&lVagt drab i B: &f" + gang.getGangStats().getGuard_kills_in_b(),
                            "&c&lVagt drab i C: &f" + gang.getGangStats().getGuard_kills_in_c(),
                            "&c&lOprettet: &f" + Utils.formatDateFromEpochMilli(gang.getCreationDateMillis())
                    ).make());

            //Members 15
            List<GangPlayer> gangMembers = new ArrayList<>();
            gang.getGangMembers().getMemberList().forEach(uuid -> gangMembers.add(gangPlayerManager.findById(uuid)));
            gangMembers.sort(Comparator.comparing(GangPlayer::getGangRank));
            List<String> memberLore = new ArrayList<>();
            gangMembers.forEach(gangMember ->
                    memberLore.add("&c&l" + gangMember.getGangRank().getName()
                                    + (gangMember.getOfflinePlayer().isOnline() ? " &a●" : " &7●")
                                    + " &f" + gangMember.getOfflinePlayer().getName() )
            );
            setItem(15, new ItemCreator(Material.PLAYER_HEAD).setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(gang.getOwnerUUID()).getName())
                    .setName("&a&lMedlemmer").addLore(memberLore).make());

            //Limits 11
            List<String> limitsLore = new ArrayList<>();
            limitsLore.add("&7&lMedlemmer: &c" + gang.getGangMembers().getMemberCount() + " &7&l/ &c" + gang.getGangMembers().getMaxMembers());
            limitsLore.add("&7&lAllierede: &c" + gang.getGangAllies().getAllyCount() + " &7&l/ &c" + gang.getGangAllies().getMaxAllies());
            limitsLore.add("&7&lRivaler: &c" + gang.getGangRivals().getRivalCount() + " &7&l/ &c" + gang.getGangRivals().getMaxRivals());
            limitsLore.add("&7&lBande-skade: &c" + gang.getGangMembers().getMemberDamagePercent() + "&7%");
            limitsLore.add("&7&lAlly-skade: &c" + gang.getGangAllies().getAllyDamagePercent() + "&7%");
            limitsLore.add("&7&lAdgang til bandeområde i &cC&7: &aja&7/&cnej");
            limitsLore.add("&7&lAdgang til bandeområde i &bB&7: &aja&7/&cnej");
            limitsLore.add("&7&lAdgang til bandeområde i &aA&7: &aja&7/&cnej");
            setItem(11, new ItemCreator(Material.IRON_HELMET)
                    .setName("&a&lBegrænsninger")
                    .addLore(limitsLore)
                    .make());

            //Allies 29
            List<String> alliedGangs = new ArrayList<>();
            gang.getGangAllies().getAlliedGangs().forEach(allyUUID ->
                    alliedGangs.add("&7- &a" + gangManager.findById(allyUUID).getName()));
            setItem(29, new ItemCreator(Material.GREEN_BANNER)
                    .setName("&a&lAllierede")
                    .addLore(alliedGangs)
                    .make());

            //Rivals 33
            List<String> rivalGangs = new ArrayList<>();
            if(gang.getGangRivals().getRivalCount() > 0) {
                gang.getGangRivals().getRivalGangs().forEach(rivalUUID ->
                        rivalGangs.add("&7- &c" + gangManager.findById(rivalUUID).getName()));
            } else {
                rivalGangs.add("&7I har ingen rivaler");
            }
            rivalGangs.add("");
            rivalGangs.add("&a&lRivaler mod jer");
            if(gang.getGangRivals().getRivalAgainstCount() > 0) {
                gang.getGangRivals().getRivalGangsAgainst().forEach(rivalAgainstUUID ->
                        rivalGangs.add("&7- &c" + gangManager.findById(rivalAgainstUUID).getName()));
            } else {
                rivalGangs.add("&7I har ingen rivaler imod jer");
            }
            setItem(33, new ItemCreator(Material.RED_BANNER)
                    .setName("&a&lRivaler")
                    .addLore(rivalGangs)
                    .make());

            //Level-up 31
            List<String> levelUpLore = new ArrayList<>();
            GangLevel nextGangLevel = gangLevelManager.getGangLevelFromInt(gang.getLevel()+1);
            for (GangLevelRequirement requirement : nextGangLevel.getGangLevelRequirements().getRequirements()) {
                int requirementAmount = requirement.getAmount();
                int requirementProgress = requirement.getProgress(gang);
                String requirementDescription = requirement.getRequirementType().getDescription();;
                boolean requirementMet = requirement.requirementMet(gang);

                levelUpLore.add((requirementMet ? " &a✓" : " &c✘")
                        + " &7" + requirementDescription
                        + " ["+requirementProgress+"/"+requirementAmount+"]");

            }
            setItem(31, new ItemCreator(Material.NETHER_STAR)
                    .setName("&a&lLevel op")
                    .addLore(levelUpLore)
                    .make());


        } else {
            setItem(22, new ItemCreator(Material.PLAYER_HEAD)
                    .setName("&a&lUventet fejl...").addLore(
                            "&cDet lader til at du er blevet vist en forkert menu.",
                            "&cKontakt venligst en admin hvis dette bliver ved!"
                    ).make());
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                player.closeInventory();
                MainGui mainGui = new MainGui(player, gang);
                mainGui.openTo(player);
            }
        }
    }
}
