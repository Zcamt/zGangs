package me.Zcamt.zgangs.listeners;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.chatinput.ChatInput;
import me.Zcamt.zgangs.chatinput.ChatInputManager;
import me.Zcamt.zgangs.chatinput.ChatIntegerInput;
import me.Zcamt.zgangs.chatinput.ChatStringInput;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.utils.ChatUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class ChatInputListener implements Listener {

    ChatInputManager chatInputManager = ZGangs.getChatInputManager();

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String chatMessage = event.getMessage();
        if(!chatInputManager.awaitsInputFrom(player)) return;
        event.setCancelled(true);
        if(chatMessage.equals("-cancel") || chatMessage.equals("-afbryd")){
            ChatUtil.sendMessage(player, Config.prefix + " &c&lDu har afbrudt chat-input processen!");
            chatInputManager.removeFromAwaitingMap(player);
            return;
        }
        ChatInput chatInput = chatInputManager.getChatInputFromPlayer(player);
        if(chatInput == null) return;
        if(chatInput instanceof ChatStringInput stringInput) {
            stringInput.supply(chatMessage);
            chatInputManager.removeFromAwaitingMap(player);
        } else if(chatInput instanceof ChatIntegerInput integerInput) {
            if(NumberUtils.isNumber(chatMessage)) {
                integerInput.supply(Integer.valueOf(chatMessage));
            } else {
                ChatUtil.sendMessage(player, Messages.invalidInput);
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        chatInputManager.removeFromAwaitingMap(player);
    }

}
