package org.activecraft.activecraftcore.exceptions

import org.bukkit.permissions.Permissible

open class NoPermissionException @JvmOverloads constructor (
    private val permissible: Permissible,
    private val permission: String,
    private val others: Boolean = false,
    message: String? = "$permission doesn't have the permission \"$permission\""
) : ActiveCraftException(message)