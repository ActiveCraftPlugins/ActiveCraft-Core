package org.activecraft.activecraftcore.commands.util

import org.activecraft.activecraftcore.exceptions.NoPermissionException
import org.activecraft.activecraftcore.messages.ActiveCraftMessageImpl
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



}