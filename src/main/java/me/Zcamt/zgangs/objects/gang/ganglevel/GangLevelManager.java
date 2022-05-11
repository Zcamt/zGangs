package me.Zcamt.zgangs.objects.gang.ganglevel;

public class GangLevelManager {

    private final GangLevel_2 gangLevel_2;
    private final GangLevel_3 gangLevel_3;
    private final int lastLevel = 3;

    public GangLevelManager() {
        this.gangLevel_2 = new GangLevel_2();
        this.gangLevel_3 = new GangLevel_3();
    }

    public GangLevel getLevelFromInt(int level) {
        if (level <= 1) level = 2;
        if (level > lastLevel) level = lastLevel;
        return switch (level) {
            case 2 -> gangLevel_2;
            case 3 -> gangLevel_3;
            default -> throw new IllegalStateException("Unexpected value: " + level);
        };
    }

    public GangLevel_2 getGangLevel_2() {
        return gangLevel_2;
    }

    public int getLastLevel() {
        return lastLevel;
    }
}
