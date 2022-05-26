package me.Zcamt.zgangs.chatinput;

import java.util.function.Consumer;

public class ChatIntegerInput extends ChatInput<Integer> {
    public ChatIntegerInput(Consumer<Integer> consumer) {
        super(consumer);
    }
}
