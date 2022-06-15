package me.Zcamt.zgangs.objects.gang.level;

public enum GangLevelRequirementType {
    MEMBER_COUNT("Antal medlemmer"),
    ALLY_COUNT("Antal allierede"),
    KILLS("Antal drab"),
    GUARD_KILLS("Antal vagt-drab i alt"),
    GUARD_KILLS_IN_C("Antal vagt-drab i C"),
    GUARD_KILLS_IN_B("Antal vagt-drab i B"),
    GUARD_KILLS_IN_A("Antal vagt-drab i A"),
    BANK_BALANCE("Penge i bandebanken"),
    DELIVER_CIGS("Cigaretter afleveret til mafia bossen"),
    DELIVER_BREAD("Brød afleveret til mafia bossen");

    private final String description;

    GangLevelRequirementType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
