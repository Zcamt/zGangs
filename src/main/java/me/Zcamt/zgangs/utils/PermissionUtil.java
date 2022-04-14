package me.Zcamt.zgangs.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PermissionUtil {

    public static boolean hasPermissionWithMessage(@NotNull Player player, @NotNull String permission, @Nullable String denyMessage) {
        if (player.isOp()) return true;
        if (player.hasPermission("*")) return true;
        if (player.hasPermission(Permissions.OVERRIDE.getPermission())) return true;
        boolean hasPerm = player.hasPermission(permission);
        if (!hasPerm) {
            ChatUtil.sendMessage(player,
                    denyMessage == null ? "&4Error: &cYou're not allowed to do that!" : denyMessage);
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

}
