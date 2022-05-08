package me.Zcamt.zgangs.objects.ganglevels;

import java.util.Arrays;
import java.util.HashMap;


public class GangLevel_3 extends GangLevel {

    public GangLevel_3() {
        super(Arrays.asList(
                "Ekstra allyplads",
                "Adgang til flipmanden i C"
        ));
        HashMap<GangLevelRequirement, Integer> levelRequirements = new HashMap<>();
        levelRequirements.put(GangLevelRequirement.BANK_BALANCE, 5000);
        levelRequirements.put(GangLevelRequirement.MEMBER_COUNT, 2);
        setLevelRequirements(levelRequirements);
    }
}
