package me.Zcamt.zgangs.chatinput;

import java.util.function.Consumer;

public abstract class ChatInput<T> {

    private final Consumer<T> consumer;

    public ChatInput(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public void supply(T t) {
        consumer.accept(t);
    }

}
