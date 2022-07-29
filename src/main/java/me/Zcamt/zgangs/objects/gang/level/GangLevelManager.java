package me.Zcamt.zgangs.objects.gang.level;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GangLevelManager {

    private final Map<Integer, GangLevel> gangLevels = new HashMap<>();

    public GangLevelManager() {
        gangLevels.put(1, new GangLevel(
                1, 3, 1,
                100, 100, false, false, false,
                1, Arrays.asList(
                "&6- &fAdgang til 3 medlemmer og 1 allieret fra start"),
                new GangLevelRequirements())
        );

        gangLevels.put(2, new GangLevel(
                2, 4, 2,
                95, 100, false, false, false,
                1, Arrays.asList(
                "&6- &fKøb adgang op til 4 medlemmer",
                "&6- &fKøb adgang op til 2 allierede",
                "&6- &fKøb adgang til 95% bandeskade"),
                new GangLevelRequirements(
                        new GangLevelRequirement(GangLevelRequirementType.BANK_BALANCE, 5000),
                        new GangLevelRequirement(GangLevelRequirementType.MEMBER_COUNT, 2)
                ))
        );

        gangLevels.put(3, new GangLevel(
                3, 4, 2,
                90, 95, true, false, false,
                2, Arrays.asList(
                "&6- &fFå adgang til 2 MOTD-linje i alt",
                "&6- &fKøb adgang til 95% allyskade",
                "&6- &fKøb adgang til 90% bandeskade",
                "&6- &fKøb adgang til bandeområde i C"),
                new GangLevelRequirements(
                        new GangLevelRequirement(GangLevelRequirementType.BANK_BALANCE, 15000),
                        new GangLevelRequirement(GangLevelRequirementType.MEMBER_COUNT, 3),
                        new GangLevelRequirement(GangLevelRequirementType.ALLY_COUNT, 1)
                ))
        );

        gangLevels.put(4, new GangLevel(
                4, 5, 3,
                90, 95, true, false, false,
                3, Arrays.asList(
                "&6- &fFå adgang til 3 MOTD-linje i alt",
                "&6- &fKøb adgang op til 5 medlemmer",
                "&6- &fKøb adgang op til 3 allierede"),
                new GangLevelRequirements(
                        new GangLevelRequirement(GangLevelRequirementType.BANK_BALANCE, 35000),
                        new GangLevelRequirement(GangLevelRequirementType.DELIVER_BREAD, 64),
                        new GangLevelRequirement(GangLevelRequirementType.DELIVER_CIGS, 1)
                ))
        );

        gangLevels.put(5, new GangLevel(
                5, 5, 3,
                85, 85, true, true, false,
                3, Arrays.asList(
                "&6- &fKøb adgang til 85% allyskade",
                "&6- &fKøb adgang til 85% bandeskade",
                "&6- &fKøb adgang til bandeområde i B"),
                new GangLevelRequirements(
                        new GangLevelRequirement(GangLevelRequirementType.BANK_BALANCE, 75000),
                        new GangLevelRequirement(GangLevelRequirementType.DELIVER_BREAD, 256),
                        new GangLevelRequirement(GangLevelRequirementType.DELIVER_CIGS, 16)
                ))
        );
    }

    public GangLevel getGangLevelFromInt(int lvl) {
        if(lvl < 1) {
            lvl = 1;
        } else if(lvl > getLastLevelInt()) {
            lvl = getLastLevelInt();
        }
        return gangLevels.get(lvl);
    }

    public int getLastLevelInt(){
        return gangLevels.keySet().stream().sorted(Comparator.reverseOrder()).toList().get(0);
    }


}
