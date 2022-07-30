package me.Zcamt.zgangs.confirmaction;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmActionManager {

    private final Map<Player, ConfirmAction> awaitingConfirmation = new HashMap<>();

    public void newConfirmation(Player player, Runnable runnable, List<String> actionDescription) {
        ConfirmAction confirmAction = new ConfirmAction(runnable, actionDescription);
        awaitingConfirmation.put(player, confirmAction);
    }

    public ConfirmAction getConfirmationFromPlayer(Player player) {
        return awaitingConfirmation.getOrDefault(player, null);
    }

    public void removeFromConfirmationMap(Player player) {
        awaitingConfirmation.remove(player);
    }

    public boolean awaitsConfirmationFrom(Player player) {
        return awaitingConfirmation.containsKey(player);
    }

}
