package me.Zcamt.zgangs.objects.gang.itemdelivery;

import me.Zcamt.zgangs.objects.gang.Gang;

public class GangItemDelivery {

    private Gang gang;
    private int breadDelivered;
    private int cigsDelivered;


    public GangItemDelivery(int breadDelivered, int cigsDelivered) {
        this.breadDelivered = breadDelivered;
        this.cigsDelivered = cigsDelivered;
    }

    public void setBreadDelivered(int breadDelivered) {
        this.breadDelivered = breadDelivered;
        gang.serialize();
    }

    public void setCigsDelivered(int cigsDelivered) {
        this.cigsDelivered = cigsDelivered;
        gang.serialize();
    }


    public int getBreadDelivered() {
        return breadDelivered;
    }

    public int getCigsDelivered() {
        return cigsDelivered;
    }

    public void setGang(Gang gang) {
        if(this.gang == null && gang != null) {
            this.gang = gang;
        }
    }
}
