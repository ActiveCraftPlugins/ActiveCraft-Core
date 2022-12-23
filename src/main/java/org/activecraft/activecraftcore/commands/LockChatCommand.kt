package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.lang.Boolean
import kotlin.Array
import kotlin.String
import kotlin.Throws

class LockChatCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("lockchat", plugin!!) {
    @Throws(ActiveCraftException::class)
    override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        ActiveCraftCore.INSTANCE.mainConfig.lockChat = Boolean.parseBoolean(args[0])
        sendMessage(sender, cmdMsg("locked-chat"))
    }

    override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String>? {
        return if (args.size == 1) listOf("true", "false") else null
    }
}