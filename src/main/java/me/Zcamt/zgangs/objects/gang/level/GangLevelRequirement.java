package me.Zcamt.zgangs.objects.gang.level;

import me.Zcamt.zgangs.objects.gang.Gang;

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

    public int getProgress(Gang gang) {
        switch (requirementType) {
            case KILLS -> {
                return gang.getGangStats().getKills();
            }
            case GUARD_KILLS -> {
                return (gang.getGangStats().getGuard_kills_in_a() +
                        gang.getGangStats().getGuard_kills_in_b() +
                        gang.getGangStats().getGuard_kills_in_c());

            }
            case GUARD_KILLS_IN_C -> {
                return gang.getGangStats().getGuard_kills_in_c();
            }
            case GUARD_KILLS_IN_B -> {
                return gang.getGangStats().getGuard_kills_in_b();
            }
            case GUARD_KILLS_IN_A -> {
                return gang.getGangStats().getGuard_kills_in_a();
            }
            case ALLY_COUNT -> {
                return gang.getGangAllies().getAlliedGangs().size();
            }
            case BANK_BALANCE -> {
                return gang.getBank();
            }
            case MEMBER_COUNT -> {
                return gang.getGangMembers().getMemberList().size();
            }
            case DELIVER_CIGS -> {
                return gang.getGangItemDelivery().getCigsDelivered();
            }
            case DELIVER_BREAD -> {
                return gang.getGangItemDelivery().getBreadDelivered();
            }
        }
        return 0;
    }

    public boolean requirementMet(Gang gang) {
        switch (requirementType) {
            case KILLS -> {
                if(gang.getGangStats().getKills() < getAmount()){
                    return false;
                }
            }
            case GUARD_KILLS -> {
                if((gang.getGangStats().getGuard_kills_in_a() +
                        gang.getGangStats().getGuard_kills_in_b() +
                        gang.getGangStats().getGuard_kills_in_c()
                ) < getAmount()){
                    return false;
                }
            }
            case GUARD_KILLS_IN_C -> {
                if(gang.getGangStats().getGuard_kills_in_c() < getAmount()){
                    return false;
                }
            }
            case GUARD_KILLS_IN_B -> {
                if(gang.getGangStats().getGuard_kills_in_b() < getAmount()){
                    return false;
                }
            }
            case GUARD_KILLS_IN_A -> {
                if(gang.getGangStats().getGuard_kills_in_a() < getAmount()){
                    return false;
                }
            }
            case ALLY_COUNT -> {
                if(gang.getGangAllies().getAlliedGangs().size() < getAmount()){
                    return false;
                }
            }
            case BANK_BALANCE -> {
                if(gang.getBank() < getAmount()){
                    return false;
                }
            }
            case MEMBER_COUNT -> {
                if(gang.getGangMembers().getMemberList().size() < getAmount()){
                    return false;
                }
            }
            case DELIVER_CIGS -> {
                if(gang.getGangItemDelivery().getCigsDelivered() < getAmount()){
                    return false;
                }
            }
            case DELIVER_BREAD -> {
                if(gang.getGangItemDelivery().getBreadDelivered() < getAmount()){
                    return false;
                }
            }
        }
        return true;
    }
}
