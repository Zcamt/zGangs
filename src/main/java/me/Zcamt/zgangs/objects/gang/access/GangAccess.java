package me.Zcamt.zgangs.objects.gang.access;

import me.Zcamt.zgangs.objects.gang.Gang;

public class GangAccess {

    private Gang gang;
    private boolean gangAreaCUnlocked;
    private boolean gangAreaBUnlocked;
    private boolean gangAreaAUnlocked;

    public GangAccess(boolean gangAreaCUnlocked, boolean gangAreaBUnlocked, boolean gangAreaAUnlocked) {
        this.gangAreaCUnlocked = gangAreaCUnlocked;
        this.gangAreaBUnlocked = gangAreaBUnlocked;
        this.gangAreaAUnlocked = gangAreaAUnlocked;
    }


    public void setGangAreaCUnlocked(boolean gangAreaCUnlocked) {
        this.gangAreaCUnlocked = gangAreaCUnlocked;
        gang.serialize();
    }

    public void setGangAreaBUnlocked(boolean gangAreaBUnlocked) {
        this.gangAreaBUnlocked = gangAreaBUnlocked;
        gang.serialize();
    }

    public void setGangAreaAUnlocked(boolean gangAreaAUnlocked) {
        this.gangAreaAUnlocked = gangAreaAUnlocked;
        gang.serialize();
    }

    public boolean isGangAreaCUnlocked() {
        return gangAreaCUnlocked;
    }

    public boolean isGangAreaBUnlocked() {
        return gangAreaBUnlocked;
    }

    public boolean isGangAreaAUnlocked() {
        return gangAreaAUnlocked;
    }

    public void setGang(Gang gang) {
        if(this.gang == null && gang != null) {
            this.gang = gang;
        }
    }
}
