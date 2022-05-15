package me.Zcamt.zgangs.objects.gang.ganglevel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GangLevelManager {

    private final Map<Integer, GangLevel> gangLevels = new HashMap<>();

    public GangLevelManager() {
        gangLevels.put(1, new GangLevel(
                1, 3, 1,
                false, false, false,
                Arrays.asList(
                "Adgang til 3 medlemmer og 1 allieret fra start"),
                new GangLevelRequirements())
        );

        gangLevels.put(2, new GangLevel(
                2, 4, 2,
                false, false, false,
                Arrays.asList(
                        "Køb adgang op til 4 medlemmer",
                        "Køb adgang op til 2 allierede"),
                new GangLevelRequirements(
                        new GangLevelRequirement(GangLevelRequirementType.BANK_BALANCE, 5000),
                        new GangLevelRequirement(GangLevelRequirementType.MEMBER_COUNT, 2)
                ))
        );
    }

    public GangLevel getGangLevelFromInt(int lvl) {
        return gangLevels.get(lvl);
    }

    public int getLastLevelInt(){
        return gangLevels.keySet().stream().sorted(Comparator.reverseOrder()).toList().get(0);
    }


}
