package me.Zcamt.zgangs.objects.gang;

import java.util.HashMap;

public enum GangRank {
    OWNER(5, "Ejer", "&4"),
    CO_OWNER(4, "Med-Ejer", "&c"),
    CAPTAIN(3, "Kaptajn", "&e"),
    MEMBER(2, "Medlem", "&a"),
    RECRUIT(1, "Rekrut", "&8");

    private final int ID;
    private final String name;
    private final String colorCode;
    private static final HashMap<Integer, GangRank> BY_ID = new HashMap<>();
    GangRank(int id, String name, String colorCode) {
        ID = id;
        this.name = name;
        this.colorCode = colorCode;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getFormatedNamed() {
        return colorCode+name;
    }

    public static GangRank getRank(int id) {
        if(id > 5) {
            id = 5;
        } else if (id < 1) {
            id = 1;
        }
        return BY_ID.get(id);
    }

    /**
     * Compare two ranks.
     *
     * @param compareTo rank to compare with
     * @return A negative integer if the compared rank is larger than instance.
     * 0 if they are equal and a positive integer if the compared rank smaller than instance.
     */
    public int compare(GangRank compareTo) {
        return this.getID() - compareTo.getID();
    }

    public boolean isHigherOrEqualThan(GangRank compareTo) {
        return this.getID() - compareTo.getID() >= 0;
    }

    public boolean isHigherThan(GangRank compareTo) {
        return this.getID() - compareTo.getID() > 0;
    }

    static {
        for(GangRank rank : values()) {
            BY_ID.put(rank.getID(), rank);
        }
    }
}
