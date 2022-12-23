package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.manager.MessageManager
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ReplyCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("reply", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = getPlayer(sender)
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER, 0)
        var message = joinArray(args, 0)
        message = replaceColorAndFormat(message)
        messageFormatter.addFormatterPattern("message", message)
        MessageManager.getMsgPartner(player)?.let { messageFormatter.setTarget(it) }
        MessageManager.msgToActiveConversation(sender, replaceColorAndFormat(message))
        sendMessage(sender, activeCraftMessage.getMessage("command.msg.format-to"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}