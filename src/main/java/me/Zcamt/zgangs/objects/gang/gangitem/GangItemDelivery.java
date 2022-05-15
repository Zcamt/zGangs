package me.Zcamt.zgangs.objects.gang.gangitem;

import me.Zcamt.zgangs.objects.gang.Gang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GangItemDelivery {

    //Todo: Should be able to serialize after setting. Should be doable by copying the setup from GangAllies fx.
    private Gang gang;
    private final Map<GangDeliveryItem, Integer> deliveredItems = new HashMap<>();

    public GangItemDelivery(Map<GangDeliveryItem, Integer> deliveredItems) {
        for (GangDeliveryItem deliveryItem : GangDeliveryItem.values()) {
            setDeliveredItem(deliveryItem, deliveredItems.getOrDefault(deliveryItem, 0));
        }
    }

    public void addToDeliveredItem(GangDeliveryItem gangDeliveryItem, int amountToAdd) {
        if (amountToAdd < 0) {
            amountToAdd = 0;
        }
        setDeliveredItem(gangDeliveryItem, getDeliveryAmount(gangDeliveryItem) + amountToAdd);
    }

    public void setDeliveredItem(GangDeliveryItem gangDeliveryItem, int amount) {
        if (amount < 0) {
            amount = 0;
        }
        deliveredItems.put(gangDeliveryItem, amount);
        gang.serialize();
    }

    public int getDeliveryAmount (GangDeliveryItem gangDeliveryItem){
        return deliveredItems.getOrDefault(gangDeliveryItem, 0);
    }

    public Map<GangDeliveryItem, Integer> getDeliveredItemsMap() {
        return (Map<GangDeliveryItem, Integer>) Collections.unmodifiableMap(deliveredItems);
    }

    public void setGang(Gang gang) {
        if(this.gang == null) {
            this.gang = gang;
        }
    }
}
