package me.Zcamt.zgangs.objects.gangitems;

import java.util.Collections;
import java.util.HashMap;

public class GangItemDelivery {

    private final HashMap<GangDeliveryItem, Integer> deliveredItems = new HashMap<>();

    public GangItemDelivery(HashMap<GangDeliveryItem, Integer> deliveredItems) {
        for (GangDeliveryItem deliveryItem : GangDeliveryItem.values()) {
            setDeliveredItem(deliveryItem, deliveredItems.getOrDefault(deliveryItem, 0));
        }
    }

    public void addToDeliveredItem(GangDeliveryItem gangDeliveryItem, int amountToAdd) {
        if (amountToAdd < 0) {
            amountToAdd = 0;
        }
        setDeliveredItem(gangDeliveryItem, amountToAdd);
    }

    public void setDeliveredItem(GangDeliveryItem gangDeliveryItem, int amount) {
        if (amount < 0) {
            amount = 0;
        }
        deliveredItems.put(gangDeliveryItem, amount);
    }

    public int getDeliveryAmount (GangDeliveryItem gangDeliveryItem){
        return deliveredItems.getOrDefault(gangDeliveryItem, 0);
    }

    public HashMap<GangDeliveryItem, Integer> getDeliveredItems() {
        return (HashMap<GangDeliveryItem, Integer>) Collections.unmodifiableMap(deliveredItems);
    }
}
