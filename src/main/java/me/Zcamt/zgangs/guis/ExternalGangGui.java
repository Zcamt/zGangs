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

public class ExternalGangGui extends GUI {

    private final Player player;
    private final Gang targetGang;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final GangLevelManager gangLevelManager = ZGangs.getGangLevelManager();

    public ExternalGangGui(Player player, Gang targetGang) {
        super(54, ChatUtil.CC("&c&lBande info " + targetGang.getName()));
        generateGuiBorder();
        this.player = player;
        this.targetGang = targetGang;

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cLuk").make());
        setItem(22, new ItemCreator(Material.BOOK)
                .setName("&a&lBande").addLore(
                        "&c&lNavn: &f" + this.targetGang.getName(),
                        "&c&lLevel: &f" + this.targetGang.getLevel(),
                        "&c&lEjer: &f" + Bukkit.getOfflinePlayer(this.targetGang.getOwnerUUID()).getName(),
                        "&c&lBank: &f" + this.targetGang.getBank(),
                        "&c&lDrab: &f" + this.targetGang.getGangStats().getKills(),
                        "&c&lDøde: &f" + this.targetGang.getGangStats().getDeaths(),
                        "&c&lVagt drab: &f" + this.targetGang.getGangStats().getTotal_guard_kills(),
                        "&c&lOfficer+ drab: &f" + this.targetGang.getGangStats().getOfficer_plus_kills(),
                        "&c&lVagt drab i A: &f" + this.targetGang.getGangStats().getGuard_kills_in_a(),
                        "&c&lVagt drab i B: &f" + this.targetGang.getGangStats().getGuard_kills_in_b(),
                        "&c&lVagt drab i C: &f" + this.targetGang.getGangStats().getGuard_kills_in_c(),
                        "&c&lOprettet: &f" + Utils.formatDateFromEpochMilli(this.targetGang.getCreationDateMillis())
                ).make());

        //Members
        List<GangPlayer> gangMembers = new ArrayList<>();
        this.targetGang.getGangMembers().getMemberList().forEach(uuid -> gangMembers.add(gangPlayerManager.findById(uuid)));
        gangMembers.sort(Comparator.comparing(GangPlayer::getGangRank));
        List<String> memberLore = new ArrayList<>();
        gangMembers.forEach(gangMember ->
                memberLore.add("&c&l" + gangMember.getGangRank().getName()
                        + (gangMember.getOfflinePlayer().isOnline() ? " &a●" : " &7●")
                        + " &f" + gangMember.getOfflinePlayer().getName())
        );
        setItem(24, new ItemCreator(Material.PLAYER_HEAD).setSkullTextureFromePlayerName(Bukkit.getOfflinePlayer(this.targetGang.getOwnerUUID()).getName())
                .setName("&a&lMedlemmer").addLore(memberLore).make());

        //Limits
        List<String> limitsLore = new ArrayList<>();
        limitsLore.add("&7&lMedlemmer: &c" + this.targetGang.getGangMembers().getMemberCount() + " &7&l/ &c" + this.targetGang.getGangMembers().getMaxMembers());
        limitsLore.add("&7&lAllierede: &c" + this.targetGang.getGangAllies().getAllyCount() + " &7&l/ &c" + this.targetGang.getGangAllies().getMaxAllies());
        limitsLore.add("&7&lRivaler: &c" + this.targetGang.getGangRivals().getRivalCount() + " &7&l/ &c" + this.targetGang.getGangRivals().getMaxRivals());
        limitsLore.add("&7&lBande-skade: &c" + this.targetGang.getGangMembers().getMemberDamagePercent() + "&7%");
        limitsLore.add("&7&lAlly-skade: &c" + this.targetGang.getGangAllies().getAllyDamagePercent() + "&7%");
        limitsLore.add("&7&lAdgang til bandeområde i &cC&7: &aja&7/&cnej");
        limitsLore.add("&7&lAdgang til bandeområde i &bB&7: &aja&7/&cnej");
        limitsLore.add("&7&lAdgang til bandeområde i &aA&7: &aja&7/&cnej");
        setItem(20, new ItemCreator(Material.IRON_HELMET)
                .setName("&a&lBegrænsninger")
                .addLore(limitsLore)
                .make());

        //Allies
        List<String> alliedGangs = new ArrayList<>();
        this.targetGang.getGangAllies().getAlliedGangs().forEach(allyUUID ->
                alliedGangs.add("&7- &a" + gangManager.findById(allyUUID).getName()));
        setItem(30, new ItemCreator(Material.GREEN_BANNER)
                .setName("&a&lAllierede")
                .addLore(alliedGangs)
                .make());

        //Rivals
        List<String> rivalGangs = new ArrayList<>();
        if (this.targetGang.getGangRivals().getRivalCount() > 0) {
            this.targetGang.getGangRivals().getRivalGangs().forEach(rivalUUID ->
                    rivalGangs.add("&7- &c" + gangManager.findById(rivalUUID).getName()));
        } else {
            rivalGangs.add("&7I har ingen rivaler");
        }
        rivalGangs.add("");
        rivalGangs.add("&a&lRivaler mod jer");
        if (this.targetGang.getGangRivals().getRivalAgainstCount() > 0) {
            this.targetGang.getGangRivals().getRivalGangsAgainst().forEach(rivalAgainstUUID ->
                    rivalGangs.add("&7- &c" + gangManager.findById(rivalAgainstUUID).getName()));
        } else {
            rivalGangs.add("&7I har ingen rivaler imod jer");
        }
        setItem(32, new ItemCreator(Material.RED_BANNER)
                .setName("&a&lRivaler")
                .addLore(rivalGangs)
                .make());

        //Level-up
        List<String> levelUpLore = new ArrayList<>();
        GangLevel nextGangLevel = gangLevelManager.getGangLevelFromInt(this.targetGang.getLevel() + 1);
        for (GangLevelRequirement requirement : nextGangLevel.getGangLevelRequirements().getRequirements()) {
            int requirementAmount = requirement.getAmount();
            int requirementProgress = requirement.getProgress(this.targetGang);
            String requirementDescription = requirement.getRequirementType().getDescription();
            ;
            boolean requirementMet = requirement.requirementMet(this.targetGang);

            levelUpLore.add((requirementMet ? " &a✓" : " &c✘")
                    + " &7" + requirementDescription
                    + " [" + requirementProgress + "/" + requirementAmount + "]");

        }
        setItem(20, new ItemCreator(Material.NETHER_STAR)
                .setName("&a&lLevel op")
                .addLore(levelUpLore)
                .make());


    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                player.closeInventory();
            }
        }
    }
}
