package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.manager.MessageManager.msgToActiveConversation
import org.activecraft.activecraftcore.manager.MessageManager.openConversation
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MsgCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("msg", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER, 1)
        val profile = getProfile(args[0])
        messageFormatter.setTarget(profile)
        isTargetSelf(sender, profile.name, true)
        var message = joinArray(args, 1)
        message = replaceColorAndFormat(message)
        messageFormatter.addFormatterPattern("message", message)
        sendMessage(sender, this.cmdMsg("format-to"))
        openConversation(sender, profile)
        msgToActiveConversation(sender, message)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getProfileNames() else null

}