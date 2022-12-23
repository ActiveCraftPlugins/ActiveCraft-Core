package org.activecraft.activecraftcore.commands.util

import org.activecraft.activecraftcore.commands.ActiveCraftCommand
import org.activecraft.activecraftcore.exceptions.NoPermissionException
import org.activecraft.activecraftcore.exceptions.SelfTargetException
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permissible

interface PermissionUtils {

    @Throws(NoPermissionException::class)
    fun checkRawPermission(permissible: Permissible, perm: String): Boolean {
        if (!permissible.hasPermission(perm)) throw NoPermissionException(
            permissible,
            perm
        )
        return true
    }

    @Throws(SelfTargetException::class)
    fun isTargetSelf(
        sender: CommandSender,
        target: CommandSender,
        activeCraftCommand: ActiveCraftCommand,
        extendedPermission: String? = null,
        ignorePermission: Boolean = false
    ) = isTargetSelf(sender, target.name, activeCraftCommand, extendedPermission, ignorePermission)

    @Throws(SelfTargetException::class)
    fun isTargetSelf(
        sender: CommandSender,
        target: String,
        activeCraftCommand: ActiveCraftCommand,
        extendedPermission: String? = null,
        ignorePermission: Boolean = false
    ): Boolean {
        val plugin = activeCraftCommand.plugin
        if (sender.name.equals(target, ignoreCase = true)) {
            val permString = ((if (plugin.permissionGroup != null) plugin.permissionGroup + "." else "")
                    + activeCraftCommand.permission + ".self" + if (extendedPermission != null) ".$extendedPermission" else "")
            if (!ignorePermission && !sender.hasPermission(permString)) throw SelfTargetException(
                sender,
                permString
            )
            return true
        }
        return false
    }

    @Throws(NoPermissionException::class)
    fun assertPermission(
        activeCraftCommand: ActiveCraftCommand,
        permissible: Permissible,
        permissionExtension: String? = null,
        permissionPrefix: String? = activeCraftCommand.plugin.permissionGroup
    ): Boolean {
        val permString = ((if (permissionPrefix != null) "$permissionPrefix." else "")
                + activeCraftCommand.permission
                + if (permissionExtension != null) ".$permissionExtension" else "")
        if (!permissible.hasPermission(permString)) throw NoPermissionException(
            permissible,
            permString
        )
        return true
    }

}