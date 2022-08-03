package me.Zcamt.zgangs.objects.gang.shop;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.level.GangLevelManager;
import me.Zcamt.zgangs.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GangShop {

    //Todo: Remember to add "is-gang-cig" as NBT boolean to blaze-rod cigs
    // Prices in config.yml file

    private final Gang gang;
    private final GangLevelManager gangLevelManager = ZGangs.getGangLevelManager();

    public GangShop(Gang gang) {
        this.gang = gang;
    }


    public int getPriceForNextMemberUpgrade(){
        int currentMax = gang.getGangMembers().getMaxMembers();
        int startMax = gangLevelManager.getGangLevelFromInt(1).getMaxMemberLimit();
        int delta = currentMax-startMax;

        return Config.memberUpgradeStartPrice*(delta*Config.memberUpgradeIncreaseCoefficient);
    }

    public int getPriceForNextAllyUpgrade(){
        int currentMax = gang.getGangAllies().getMaxAllies();
        int startMax = gangLevelManager.getGangLevelFromInt(1).getMaxAllyLimit();
        int delta = currentMax-startMax;

        return Config.memberUpgradeStartPrice*(delta*Config.memberUpgradeIncreaseCoefficient);
    }

    public int getPriceForNextGangDmgUpgrade(){
        int currentGangDmg = gang.getGangMembers().getMemberDamagePercent();
        int startGangDmg = gangLevelManager.getGangLevelFromInt(1).getGangDamageLimit();
        int delta = currentGangDmg-startGangDmg;

        return Config.gangDamageUpgradeStartPrice*(delta*Config.gangDamageUpgradeIncreaseCoefficient);
    }

    public int getPriceForNextAllyDmgUpgrade(){
        int currentAllyDmg = gang.getGangAllies().getAllyDamagePercent();
        int startAllyDmg = gangLevelManager.getGangLevelFromInt(1).getAllyDamageLimit();
        int delta = currentAllyDmg-startAllyDmg;

        return Config.allyDamageUpgradeStartPrice*(delta*Config.allyDamageUpgradeIncreaseCoefficient);
    }


    public int getPriceForGangAreaC() {
        return Config.gangAreaCPrice;
    }

    public int getPriceForGangAreaB() {
        return Config.gangAreaBPrice;
    }

    public int getPriceForGangAreaA() {
        return Config.gangAreaAPrice;
    }

    public int getCigPrice() {
        return Config.cigPrice;
    }

    public void giveCigTo(Player player, int amount) {
        ItemStack cigarette = new ItemCreator(Material.BLAZE_ROD)
                .setName("&6Cigaret")
                .addLore("&7Kan afleveres til mafia bossen")
                .setAmount(amount)
                .make();
        player.getInventory().addItem(cigarette);
    }

}
