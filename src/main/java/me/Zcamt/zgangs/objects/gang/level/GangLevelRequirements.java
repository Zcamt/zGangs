package me.Zcamt.zgangs.objects.gang.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GangLevelRequirements {

    private final List<GangLevelRequirement> requirements = new ArrayList<>();

    public GangLevelRequirements(GangLevelRequirement... requirements) {
        this.requirements.addAll(Arrays.asList(requirements));
    }

    public List<GangLevelRequirement> getRequirements() {
        return requirements;
    }
}
