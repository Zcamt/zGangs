package me.Zcamt.zgangs.utils;

public enum Messages {
    INVALID_USAGE("&cInvalid usage. Usage: {usage}"),
    NO_PERMISSION("&cError: You don't have permission to do that"),
    NOT_IN_A_GANG("&cError: You need to be in a gang to do that");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
