package me.Zcamt.zgangs.objects.gang.level;

import me.Zcamt.zgangs.objects.gang.Gang;

import java.util.List;

public class GangLevel {

    private final int level;
    private final int maxMemberLimit;
    private final int maxAllyLimit;
    private final int gangDamageLimit;
    private final int allyDamageLimit;
    private final boolean gangAreaCPurchaseable;
    private final boolean gangAreaBPurchaseable;
    private final boolean gangAreaAPurchaseable;
    private final int motdLines;
    private final List<String> newFeaturesForLevel;
    private final GangLevelRequirements gangLevelRequirements;

    public GangLevel(int level, int maxMemberLimit, int maxAllyLimit,
                     int gangDamageLimit, int allyDamageLimit, boolean gangAreaCPurchaseable, boolean gangAreaBPurchaseable, boolean gangAreaAPurchaseable,
                     int motdLines, List<String> newFeaturesForLevel, GangLevelRequirements gangLevelRequirements) {
        this.level = level;
        this.maxMemberLimit = maxMemberLimit;
        this.maxAllyLimit = maxAllyLimit;
        this.gangDamageLimit = gangDamageLimit;
        this.allyDamageLimit = allyDamageLimit;
        this.gangAreaCPurchaseable = gangAreaCPurchaseable;
        this.gangAreaBPurchaseable = gangAreaBPurchaseable;
        this.gangAreaAPurchaseable = gangAreaAPurchaseable;
        this.motdLines = motdLines;
        this.newFeaturesForLevel = newFeaturesForLevel;
        this.gangLevelRequirements = gangLevelRequirements;
    }


    public boolean requirementsMet(Gang gang) {
        if(this.level == 1) {
            return false;
        }
        for (GangLevelRequirement requirement : gangLevelRequirements.getRequirements()) {
            return requirement.requirementMet(gang);
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

    public int getGangDamageLimit() {
        return gangDamageLimit;
    }

    public int getAllyDamageLimit() {
        return allyDamageLimit;
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
