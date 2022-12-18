package org.activecraft.activecraftcore.exceptions;

import lombok.Getter;
import org.bukkit.permissions.Permissible;

@Getter
public class NoPermissionException extends ActiveCraftException {

    private final String permission;
    private final Permissible permissible;
    private final boolean others;

    public NoPermissionException(String message, Permissible permissible, String permission, boolean others) {
        super(message);
        this.permissible = permissible;
        this.permission = permission;
        this.others = others;
    }

    public NoPermissionException(Permissible permissible, String permission, boolean others) {
        this(permissible + "doesn't have the permission \"" + permission + "\"", permissible, permission, others);
    }

    public NoPermissionException(Permissible permissible, String permission) {
        this(permissible + "doesn't have the permission \"" + permission + "\"", permissible, permission, false);
    }
}
