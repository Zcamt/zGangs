package me.Zcamt.zgangs.utils;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Messages;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PermissionUtil {

    public static boolean hasPermissionWithMessage(@NotNull Player player, @NotNull String permission, @Nullable String denyMessage) {
        if (player.isOp()) return true;
        if (player.hasPermission("*")) return true;
        if (player.hasPermission(Permissions.OVERRIDE.getPermission())) return true;
        boolean hasPerm = player.hasPermission(permission);
        if (!hasPerm) {
            ChatUtil.sendMessage(player,
                    denyMessage == null ? Messages.noPerm : denyMessage);
        }
        return hasPerm;
    }

    public static boolean hasPermissionWithMessage(@NotNull Player player, @NotNull List<String> permissions, @Nullable String denyMessage) {
        for (String permission : permissions) {
            if (hasPermissionWithMessage(player, permission, denyMessage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPermission(@NotNull Player player, @NotNull String permission) {
        if (player.isOp()) return true;
        if (player.hasPermission("*")) return true;
        if (player.hasPermission(Permissions.OVERRIDE.getPermission())) return true;
        return player.hasPermission(permission);
    }

    public static boolean hasPermission(@NotNull Player player, @NotNull List<String> permissions) {
        for (String permission : permissions) {
            if (hasPermission(player, permission)) {
                return true;
            }
        }
        return false;
    }

    public static CompletableFuture<Boolean> isGuard(@NotNull UUID playerUUID) {
        List<String> guardRanks = Arrays.asList(
                "pvagt", "cvagt", "bvagt", "avagt", "officer", "inspektør", "vicedirektør", "direktør"
        );
        return ZGangs.getLuckperms().getUserManager().loadUser(playerUUID)
                .thenApplyAsync(user -> {
                    Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
                    return inheritedGroups.stream().anyMatch(g ->
                            guardRanks.contains(g.getName().toLowerCase()));
                });
    }
    public static CompletableFuture<Boolean> isOfficerPlus(@NotNull UUID playerUUID) {
        List<String> guardRanks = Arrays.asList(
                "officer", "inspektør", "vicedirektør", "direktør"
        );
        return ZGangs.getLuckperms().getUserManager().loadUser(playerUUID)
                .thenApplyAsync(user -> {
                    Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
                    return inheritedGroups.stream().anyMatch(g ->
                            guardRanks.contains(g.getName().toLowerCase()));
                });
    }

}
