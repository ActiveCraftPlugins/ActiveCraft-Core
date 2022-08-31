package de.cplaiz.activecraftcore.commands.util

import de.cplaiz.activecraftcore.exceptions.NoPermissionException
import de.cplaiz.activecraftcore.messages.ActiveCraftMessageImpl
import org.bukkit.permissions.Permissible

interface PermissionUtils {

    @Throws(NoPermissionException::class)
    fun checkRawPermission(permissible: Permissible, perm: String): Boolean {
        if (!permissible.hasPermission(perm)) throw NoPermissionException(permissible, perm)
        return true
    }



}