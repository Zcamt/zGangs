package me.Zcamt.zgangs.objects.gang.ganglevel;

import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.gangitem.GangDeliveryItem;
import me.Zcamt.zgangs.objects.gang.gangstats.GangStat;

import java.util.HashMap;
import java.util.List;

public class GangLevel {
    //Todo: Implement "limits" such as how many members you can upgrade to at each level

    private final int level;
    private final int maxMemberLimit;
    private final int maxAllyLimit;
    private final boolean gangAreaCPurchaseable;
    private final boolean gangAreaBPurchaseable;
    private final boolean gangAreaAPurchaseable;
    private final List<String> newFeaturesForLevel;
    private final GangLevelRequirements gangLevelRequirements;
    public GangLevel(int level, int maxMemberLimit, int maxAllyLimit,
                     boolean gangAreaCPurchaseable, boolean gangAreaBPurchaseable, boolean gangAreaAPurchaseable,
                     List<String> newFeaturesForLevel, GangLevelRequirements gangLevelRequirements) {
        this.level = level;
        this.maxMemberLimit = maxMemberLimit;
        this.maxAllyLimit = maxAllyLimit;
        this.gangAreaCPurchaseable = gangAreaCPurchaseable;
        this.gangAreaBPurchaseable = gangAreaBPurchaseable;
        this.gangAreaAPurchaseable = gangAreaAPurchaseable;
        this.newFeaturesForLevel = newFeaturesForLevel;
        this.gangLevelRequirements = gangLevelRequirements;
    }


    public boolean requirementsMet(Gang gang) {
        if(this.level == 1) {
            return false;
        }
        for (GangLevelRequirement requirement : gangLevelRequirements.getRequirements()){
            GangLevelRequirementType requirementType = requirement.getRequirementType();
            switch (requirementType) {
                case KILLS -> {
                    if(gang.getGangStats().getStatAmount(GangStat.KILLS) < requirement.getAmount()){
                        return false;
                    }
                }
                case ALLY_COUNT -> {
                    if(gang.getGangAllies().getAlliedGangs().size() < requirement.getAmount()){
                        return false;
                    }
                }
                case BANK_BALANCE -> {
                    if(gang.getBank() < requirement.getAmount()){
                        return false;
                    }
                }
                case MEMBER_COUNT -> {
                    if(gang.getGangMembers().getMemberList().size() < requirement.getAmount()){
                        return false;
                    }
                }
                case DELIVER_CIGS -> {
                    if(gang.getGangItemDelivery().
                            getDeliveryAmount(GangDeliveryItem.CIG) < requirement.getAmount()){
                        return false;
                    }
                }
                case DELIVER_BREAD -> {
                    if(gang.getGangItemDelivery().
                            getDeliveryAmount(GangDeliveryItem.BREAD) < requirement.getAmount()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxMemberLimit() {
        return maxMemberLimit;
    }

    public int getMaxAllyLimit() {
        return maxAllyLimit;
    }

    public boolean isGangAreaCPurchaseable() {
        return gangAreaCPurchaseable;
    }

    public boolean isGangAreaBPurchaseable() {
        return gangAreaBPurchaseable;
    }

    public boolean isGangAreaAPurchaseable() {
        return gangAreaAPurchaseable;
    }

    public List<String> getNewFeaturesForLevel() {
        return newFeaturesForLevel;
    }

    public GangLevelRequirements getGangLevelRequirements() {
        return gangLevelRequirements;
    }

    public int getCost(){
        for (GangLevelRequirement requirement : gangLevelRequirements.getRequirements()) {
            GangLevelRequirementType requirementType = requirement.getRequirementType();
            if(requirementType == GangLevelRequirementType.BANK_BALANCE) {
                return requirement.getAmount();
            }
        }
        return 0;
    }
}
