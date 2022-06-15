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
                Arrays.asList(
                "Adgang til 3 medlemmer og 1 allieret fra start"),
                new GangLevelRequirements())
        );

        gangLevels.put(2, new GangLevel(
                2, 4, 2,
                95, 100, false, false, false,
                Arrays.asList(
                        "Køb adgang op til 4 medlemmer",
                        "Køb adgang op til 2 allierede",
                        "Nedset bandeskade til 95%"),
                new GangLevelRequirements(
                        new GangLevelRequirement(GangLevelRequirementType.BANK_BALANCE, 5000),
                        new GangLevelRequirement(GangLevelRequirementType.MEMBER_COUNT, 2)
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
