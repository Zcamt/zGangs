package me.Zcamt.zgangs.utils;

import org.jetbrains.annotations.NotNull;

public enum Messages {
    INVALID_USAGE("&cInvalid usage. Usage: {usage}"),
    NO_PERMISSION("&cError: You don't have permission to do that"),
    NOT_IN_A_GANG("&cError: You need to be in a gang to do that"),
    INVALID_PLAYER("&cError: That isn't a valid player"),
    INVALID_GANG("&cError: That isn't a valid gang"),
    INVALID_GANGNAME("&cError: That isn't a valid name for your gang");

    private final String message;
    private final String toString;

    Messages(String message) {
        this.message = message;
        this.toString = message;
    }

    @NotNull
    public String toString() {
        return this.toString;
    }

    public String getMessage() {
        return message;
    }
}
