package me.Zcamt.zgangs.confirmaction;

import java.util.List;

public class ConfirmAction {

    private final Runnable runnableFuture;
    private final List<String> actionDescription;

    public ConfirmAction(Runnable runnableFuture, List<String> actionDescription) {
        this.runnableFuture = runnableFuture;
        this.actionDescription = actionDescription;
    }

    public void run() {
        runnableFuture.run();
    }

    public List<String> getActionDescription() {
        return actionDescription;
    }
}
