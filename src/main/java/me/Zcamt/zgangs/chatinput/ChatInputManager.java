package me.Zcamt.zgangs.chatinput;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChatInputManager {

    private final Map<Player, ChatInput> awaitingChatInput = new HashMap<>();

    public void newStringInput(Player player, Consumer<String> consumer) {
        ChatInput<String> chatInput = new ChatStringInput(consumer);
        awaitingChatInput.put(player, chatInput);
    }

    public void newIntInput(Player player, Consumer<Integer> consumer) {
        ChatInput<Integer> chatInput = new ChatIntegerInput(consumer);
        awaitingChatInput.put(player, chatInput);
    }

    public ChatInput getChatInputFromPlayer(Player player) {
        return awaitingChatInput.getOrDefault(player, null);
    }

    public void removeFromAwaitingMap(Player player) {
        awaitingChatInput.remove(player);
    }

    public boolean awaitsInputFrom(Player player) {
        return awaitingChatInput.containsKey(player);
    }
}
