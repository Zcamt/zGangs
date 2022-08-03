package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.level.GangLevel;
import me.Zcamt.zgangs.objects.gang.level.GangLevelManager;
import me.Zcamt.zgangs.objects.gang.shop.GangShop;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GangShopGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangShop gangShop;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final GangLevelManager gangLevelManager = ZGangs.getGangLevelManager();

    protected GangShopGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBande butik"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangShop = new GangShop(playerGang);
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        //Membercount
        int currentMaxMembers = gang.getGangMembers().getMaxMembers();
        int maxAllowedMembersForLevel = gangLevelManager.getGangLevelFromInt(gang.getLevel()).getMaxMemberLimit();
        setItem(11, new ItemCreator()
                .setMaterial(currentMaxMembers < maxAllowedMembersForLevel ? Material.PLAYER_HEAD : Material.RED_DYE)
                .setName(currentMaxMembers < maxAllowedMembersForLevel ? "&aEkstra plads til medlemmer" : "&cEkstra plads til medlemmer - Låst")
                .addLore("&7Klik her for at:",
                        "&6- &fOpgradere bandens maks antal medlemmer",
                        "&6- &f&c" + currentMaxMembers + "&f -> &a" + (currentMaxMembers + 1),
                        "&6- &fPris:" + gangShop.getPriceForNextMemberUpgrade())
                .make());

        //Allycount
        int currentMaxAlly = gang.getGangAllies().getMaxAllies();
        int maxAllowedAllyForLevel = gangLevelManager.getGangLevelFromInt(gang.getLevel()).getMaxAllyLimit();
        setItem(20, new ItemCreator()
                .setMaterial(currentMaxAlly < maxAllowedAllyForLevel ? Material.GREEN_BANNER : Material.RED_DYE)
                .setName(currentMaxAlly < maxAllowedAllyForLevel ? "&aEkstra plads til allierede" : "&cEkstra plads til allierede - Låst")
                .addLore("&7Klik her for at:",
                        "&6- &fOpgradere bandens maks antal allierede",
                        "&6- &f&c" + currentMaxAlly + "&f -> &a" + (currentMaxAlly + 1),
                        "&6- &fPris:" + gangShop.getPriceForNextAllyUpgrade())
                .make());
        //setItem(29, );

        //Gangdmg
        int currentGangDmg = gang.getGangMembers().getMemberDamagePercent();
        int maxAllowedGangDmgForLevel = gangLevelManager.getGangLevelFromInt(gang.getLevel()).getGangDamageLimit();
        setItem(13, new ItemCreator()
                .setMaterial(currentGangDmg < maxAllowedGangDmgForLevel ? Material.DIAMOND_SWORD : Material.RED_DYE)
                .setName(currentGangDmg < maxAllowedGangDmgForLevel ? "&aMindre bandeskade" : "&cMindre bandeskade - Låst")
                .addLore("&7Klik her for at:",
                        "&6- &fMinimere skaden mellem bandemedlemmer",
                        "&6- &f&c" + currentGangDmg + "%&f -> &a" + (currentGangDmg - 5) + "%",
                        "&6- &fPris:" + gangShop.getPriceForNextGangDmgUpgrade())
                .make());

        //Allydmg
        int currentAllyDmg = gang.getGangAllies().getAllyDamagePercent();
        int maxAllowedAllyDmgForLevel = gangLevelManager.getGangLevelFromInt(gang.getLevel()).getGangDamageLimit();
        setItem(22, new ItemCreator()
                .setMaterial(currentAllyDmg < maxAllowedAllyDmgForLevel ? Material.GOLDEN_SWORD : Material.RED_DYE)
                .setName(currentAllyDmg < maxAllowedAllyDmgForLevel ? "&aMindre allyskade" : "&cMindre allyskade - Låst")
                .addLore("&7Klik her for at:",
                        "&6- &fMinimere skaden mellem allierede",
                        "&6- &f&c" + currentAllyDmg + "%&f -> &a" + (currentAllyDmg - 5) + "%",
                        "&6- &fPris:" + gangShop.getPriceForNextAllyDmgUpgrade())
                .make());
        //setItem(31, );

        GangLevel gangLevel = gangLevelManager.getGangLevelFromInt(gang.getLevel());

        //Area C
        setItem(15, new ItemCreator()
                .setMaterial(gang.getGangAccess().isGangAreaCUnlocked()
                        ? (gangLevel.isGangAreaCPurchaseable() ? Material.IRON_BLOCK : Material.RED_DYE)
                        : Material.GREEN_STAINED_GLASS_PANE)
                .setName("&aBandeområde i C" + (gang.getGangAccess().isGangAreaCUnlocked()
                        ? (gangLevel.isGangAreaCPurchaseable() ? "" : "&c - Låst")
                        : " - Købt"))
                .addLore("&7Klik her for at:",
                        "&6- &fKøbe adgang til bandeområdet i C",
                        "&6- &fPris:" + gangShop.getPriceForGangAreaC())
                .make());

        //Area B
        setItem(24, new ItemCreator()
                .setMaterial(gang.getGangAccess().isGangAreaBUnlocked()
                        ? (gangLevel.isGangAreaBPurchaseable() ? Material.IRON_BLOCK : Material.RED_DYE)
                        : Material.GREEN_STAINED_GLASS_PANE)
                .setName("&aBandeområde i B" + (gang.getGangAccess().isGangAreaBUnlocked()
                        ? (gangLevel.isGangAreaBPurchaseable() ? "" : "&c - Låst")
                        : " - Købt"))
                .addLore("&7Klik her for at:",
                        "&6- &fKøbe adgang til bandeområdet i B",
                        "&6- &fPris:" + gangShop.getPriceForGangAreaB())
                .make());

        //Area A
        setItem(33, new ItemCreator()
                .setMaterial(gang.getGangAccess().isGangAreaAUnlocked()
                        ? (gangLevel.isGangAreaAPurchaseable() ? Material.IRON_BLOCK : Material.RED_DYE)
                        : Material.GREEN_STAINED_GLASS_PANE)
                .setName("&aBandeområde i A" + (gang.getGangAccess().isGangAreaAUnlocked()
                        ? (gangLevel.isGangAreaAPurchaseable() ? "" : "&c - Låst")
                        : " - Købt"))
                .addLore("&7Klik her for at:",
                        "&6- &fKøbe adgang til bandeområdet i A",
                        "&6- &fPris:" + gangShop.getPriceForGangAreaA())
                .make());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());

        setItem(53, new ItemCreator(Material.ARROW).setName("&cItem butik")
                .addLore("&7Klik her for at:",
                        "&6- &fGå til item butikken")
                .make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        if (!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                MainGui mainGui = new MainGui(player, gang);
                mainGui.openTo(player);
            }

            case PLAYER_HEAD -> {
                //Upgrade member count
                int price = gangShop.getPriceForNextMemberUpgrade();
                if (gang.getBank() < price) {
                    ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                    return;
                }
                gang.setBank(gang.getBank() - price);
                gang.getGangMembers().setMaxMembers(gang.getGangMembers().getMaxMembers() + 1);
                //Todo: Message for the rest of the gang
                GangShopGui gangShopGui = new GangShopGui(player, gang);
                gangShopGui.openTo(player);
            }

            case GREEN_BANNER -> {
                //Upgrade ally count
                int price = gangShop.getPriceForNextAllyUpgrade();
                if (gang.getBank() < price) {
                    ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                    return;
                }
                gang.setBank(gang.getBank() - price);
                gang.getGangAllies().setMaxAllies(gang.getGangAllies().getMaxAllies() + 1);
                //Todo: Message for the rest of the gang
                GangShopGui gangShopGui = new GangShopGui(player, gang);
                gangShopGui.openTo(player);
            }

            case DIAMOND_SWORD -> {
                //Upgrade gang damage
                int price = gangShop.getPriceForNextGangDmgUpgrade();
                if (gang.getBank() < price) {
                    ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                    return;
                }
                gang.setBank(gang.getBank() - price);
                gang.getGangMembers().setMemberDamagePercent(gang.getGangMembers().getMemberDamagePercent() - 5);
                //Todo: Message for the rest of the gang
                GangShopGui gangShopGui = new GangShopGui(player, gang);
                gangShopGui.openTo(player);
            }

            case GOLDEN_SWORD -> {
                //Upgrade ally damage
                int price = gangShop.getPriceForNextAllyDmgUpgrade();
                if (gang.getBank() < price) {
                    ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                    return;
                }
                gang.setBank(gang.getBank() - price);
                gang.getGangAllies().setAllyDamagePercent(gang.getGangAllies().getAllyDamagePercent() - 5);
                //Todo: Message for the rest of the gang
                GangShopGui gangShopGui = new GangShopGui(player, gang);
                gangShopGui.openTo(player);
            }

            case IRON_BLOCK -> {
                //Buy gangarea C
                int price = gangShop.getPriceForGangAreaC();
                if (gang.getBank() < price) {
                    ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                    return;
                }
                gang.setBank(gang.getBank() - price);
                gang.getGangAccess().setGangAreaCUnlocked(true);
                //Todo: Message for the rest of the gang
                GangShopGui gangShopGui = new GangShopGui(player, gang);
                gangShopGui.openTo(player);
            }

            case GOLD_BLOCK -> {
                //Buy gangarea B
                int price = gangShop.getPriceForGangAreaB();
                if (gang.getBank() < price) {
                    ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                    return;
                }
                gang.setBank(gang.getBank() - price);
                gang.getGangAccess().setGangAreaBUnlocked(true);
                //Todo: Message for the rest of the gang
                GangShopGui gangShopGui = new GangShopGui(player, gang);
                gangShopGui.openTo(player);
            }

            case DIAMOND_BLOCK -> {
                //Buy gangarea A
                int price = gangShop.getPriceForGangAreaA();
                if (gang.getBank() < price) {
                    ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                    return;
                }
                gang.setBank(gang.getBank() - price);
                gang.getGangAccess().setGangAreaAUnlocked(true);
                //Todo: Message for the rest of the gang
                GangShopGui gangShopGui = new GangShopGui(player, gang);
                gangShopGui.openTo(player);
            }

            case ARROW -> {
                //Item butik
                GangItemShopGui gangItemShopGui = new GangItemShopGui(player, gang);
                gangItemShopGui.openTo(player);
            }
        }
    }

}


class GangItemShopGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangShop gangShop;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected GangItemShopGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lItem butik"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangShop = new GangShop(playerGang);
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        setItem(22, new ItemCreator(Material.BLAZE_ROD)
                .setName("&6Cigaret")
                .addLore("&7Venstre-klik her for at:",
                        "&6- &fKøbe en cigaret",
                        "",
                        "&7Højre-klik her for at:",
                        "&6- &fKøbe fem cigaretter",
                        "",
                        "&6- &fPris pr stk:" + gangShop.getCigPrice())
                .make());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());

        setItem(45, new ItemCreator(Material.ARROW).setName("&cItem butik")
                .addLore("&7Klik her for at:",
                        "&6- &fGå til bande butikken")
                .make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        if (!gangPlayer.isInGang()) return;
        boolean isLeftClick = event.isLeftClick();
        boolean isRightClick = event.isRightClick();
        switch (clickedItem.getType()) {
            case BLAZE_ROD -> {
                //Buy cigaret
                int price = gangShop.getCigPrice();
                if(isLeftClick) {
                    if (gang.getBank() < price) {
                        ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                        return;
                    }
                    gang.setBank(gang.getBank() - price);
                    gangShop.giveCigTo(player, 1);
                } else if (isRightClick) {
                    if (gang.getBank() < (price*5)) {
                        ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                        return;
                    }
                    gang.setBank(gang.getBank() - (price*5));
                    gangShop.giveCigTo(player, 5);
                }
            }

            case BARRIER -> {
                MainGui mainGui = new MainGui(player, gang);
                mainGui.openTo(player);
            }

            case ARROW -> {
                GangShopGui gangShopGui = new GangShopGui(player, gang);
                gangShopGui.openTo(player);
            }
        }
    }
}
