package me.Zcamt.zgangs.objects.gang;

import com.google.common.collect.Maps;

import java.util.HashMap;

public enum GangRank {
    OWNER(5),
    CO_OWNER(4),
    CAPTAIN(3),
    MEMBER(2),
    RECRUIT(1);

    private final int ID;
    private static final HashMap<Integer, GangRank> BY_ID = new HashMap<>();
    GangRank(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public static GangRank getRank(int id) {
        if(id > 5) {
            id = 5;
        } else if (id < 1) {
            id = 1;
        }
        return BY_ID.get(id);
    }

    static {
        for(GangRank rank : values()) {
            BY_ID.put(rank.getID(), rank);
        }
    }
}
