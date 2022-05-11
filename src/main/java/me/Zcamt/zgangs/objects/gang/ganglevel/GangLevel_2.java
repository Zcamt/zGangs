package me.Zcamt.zgangs.objects.gang.ganglevel;

import java.util.Arrays;
import java.util.HashMap;


public class GangLevel_2 extends GangLevel {

    public GangLevel_2() {
        super(Arrays.asList(
                "Ekstra medlemsplads",
                "Adgang til bandeområde i C"
        ));
        HashMap<GangLevelRequirement, Integer> levelRequirements = new HashMap<>();
        levelRequirements.put(GangLevelRequirement.BANK_BALANCE, 5000);
        levelRequirements.put(GangLevelRequirement.MEMBER_COUNT, 2);
        setLevelRequirements(levelRequirements);
    }
}
