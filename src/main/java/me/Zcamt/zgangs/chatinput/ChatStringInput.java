package me.Zcamt.zgangs.chatinput;

import java.util.function.Consumer;

public class ChatStringInput extends ChatInput<String> {
    public ChatStringInput(Consumer<String> consumer) {
        super(consumer);
    }
}
