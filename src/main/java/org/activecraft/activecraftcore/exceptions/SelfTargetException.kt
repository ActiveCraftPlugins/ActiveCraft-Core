package org.activecraft.activecraftcore.exceptions

import org.bukkit.command.CommandSender

class SelfTargetException @JvmOverloads constructor(
    commandSender: CommandSender,
    permission: String,
    message: String? = "${commandSender.name} doesn't have the permission to target themself (\"" + permission + "\")"
) : NoPermissionException(commandSender, permission, false, message)