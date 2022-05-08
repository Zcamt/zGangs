package me.Zcamt.zgangs.objects.ganglevels;

import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gangitems.GangDeliveryItem;

import java.util.HashMap;
import java.util.List;

public abstract class GangLevel {
    //Todo: Implement "limits" such as how many members you can upgrade to at each level

    private final HashMap<GangLevelRequirement, Integer> levelRequirements = new HashMap<>();
    private final List<String> newFeaturesForLevel;

    public GangLevel(List<String> newFeaturesForLevel) {
        this.newFeaturesForLevel = newFeaturesForLevel;
    }

    public void setLevelRequirements(HashMap<GangLevelRequirement, Integer> levelRequirements){
        this.levelRequirements.clear();
        this.levelRequirements.putAll(levelRequirements);
    }

    public HashMap<GangLevelRequirement, Integer> getLevelRequirements() {
        return levelRequirements;
    }

    public List<String> getNewFeaturesForLevel() {
        return newFeaturesForLevel;
    }

    public int getCost(){
        return levelRequirements.getOrDefault(GangLevelRequirement.BANK_BALANCE, 0);
    }

    public boolean requirementsMet(Gang gang) {
        for (GangLevelRequirement requirement : levelRequirements.keySet()){
            switch (requirement) {
                case KILLS -> {
                    if(gang.getKills() < levelRequirements.get(requirement)){
                        return false;
                    }
                }
                case ALLY_COUNT -> {
                    if(gang.getAlliedGangs().size() < levelRequirements.get(requirement)){
                        return false;
                    }
                }
                case BANK_BALANCE -> {
                    if(gang.getBank() < levelRequirements.get(requirement)){
                        return false;
                    }
                }
                case MEMBER_COUNT -> {
                    if(gang.getMemberList().size() < levelRequirements.get(requirement)){
                        return false;
                    }
                }
                case DELIVER_CIGS -> {
                    if(gang.getGangItemDelivery().
                            getDeliveryAmount(GangDeliveryItem.CIG) < levelRequirements.get(requirement)){
                        return false;
                    }
                }
                case DELIVER_BREAD -> {
                    if(gang.getGangItemDelivery().
                            getDeliveryAmount(GangDeliveryItem.BREAD) < levelRequirements.get(requirement)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
