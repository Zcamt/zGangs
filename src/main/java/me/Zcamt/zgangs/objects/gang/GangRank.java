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

    public static boolean higherThanOrEqual(GangRank rank1, GangRank rank2) {
        return rank1.getID() >= rank2.getID();
    };

    static {
        for(GangRank rank : values()) {
            BY_ID.put(rank.getID(), rank);
        }
    }
}
