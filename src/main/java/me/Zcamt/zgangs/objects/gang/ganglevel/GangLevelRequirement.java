package me.Zcamt.zgangs.objects.gang.ganglevel;

import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.gangitem.GangDeliveryItem;
import me.Zcamt.zgangs.objects.gang.gangstats.GangStat;

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
                return gang.getGangStats().getStatAmount(GangStat.KILLS);
            }
            case GUARD_KILLS -> {
                return (gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_A) +
                        gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_B) +
                        gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_C));

            }
            case GUARD_KILLS_IN_C -> {
                return gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_C);
            }
            case GUARD_KILLS_IN_B -> {
                return gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_B);
            }
            case GUARD_KILLS_IN_A -> {
                return gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_A);
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
                return gang.getGangItemDelivery().getDeliveryAmount(GangDeliveryItem.CIG);
            }
            case DELIVER_BREAD -> {
                return gang.getGangItemDelivery().getDeliveryAmount(GangDeliveryItem.BREAD);
            }
        }
        return 0;
    }

    public boolean requirementMet(Gang gang) {
        switch (requirementType) {
            case KILLS -> {
                if(gang.getGangStats().getStatAmount(GangStat.KILLS) < getAmount()){
                    return false;
                }
            }
            case GUARD_KILLS -> {
                if((gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_A) +
                        gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_B) +
                        gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_C)
                ) < getAmount()){
                    return false;
                }
            }
            case GUARD_KILLS_IN_C -> {
                if(gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_C) < getAmount()){
                    return false;
                }
            }
            case GUARD_KILLS_IN_B -> {
                if(gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_B) < getAmount()){
                    return false;
                }
            }
            case GUARD_KILLS_IN_A -> {
                if(gang.getGangStats().getStatAmount(GangStat.GUARD_KILLS_IN_A) < getAmount()){
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
                if(gang.getGangItemDelivery().
                        getDeliveryAmount(GangDeliveryItem.CIG) < getAmount()){
                    return false;
                }
            }
            case DELIVER_BREAD -> {
                if(gang.getGangItemDelivery().
                        getDeliveryAmount(GangDeliveryItem.BREAD) < getAmount()){
                    return false;
                }
            }
        }
        return true;
    }
}
