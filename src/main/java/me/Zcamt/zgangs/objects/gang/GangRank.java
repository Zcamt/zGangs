package me.Zcamt.zgangs.objects.gang;

import java.util.HashMap;

public enum GangRank {
    OWNER(5, "Ejer"),
    CO_OWNER(4, "Med-Ejer"),
    CAPTAIN(3, "Kaptajn"),
    MEMBER(2, "Medlem"),
    RECRUIT(1, "Rekrut");

    private final int ID;
    private final String name;
    private static final HashMap<Integer, GangRank> BY_ID = new HashMap<>();
    GangRank(int id, String name) {
        ID = id;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
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

    static {
        for(GangRank rank : values()) {
            BY_ID.put(rank.getID(), rank);
        }
    }
}
