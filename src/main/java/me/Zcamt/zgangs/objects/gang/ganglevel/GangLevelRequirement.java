package me.Zcamt.zgangs.objects.gang.ganglevel;

public class GangLevelRequirement {

    private final GangLevelRequirementType requirementType;
    private final int amount;

    public GangLevelRequirement(GangLevelRequirementType requirementType, int amount) {
        this.requirementType = requirementType;
        this.amount = amount;
    }

    public GangLevelRequirementType getRequirementType() {
        return requirementType;
    }

    public int getAmount() {
        return amount;
    }
}
