package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.manager.NickManager.nick
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class NickCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("nick", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var args = args
        assertArgsLength(args, ComparisonType.GREATER, 0)
        val type = if (args.size == 1 || of(args[0]) == null) CommandTargetType.SELF else CommandTargetType.OTHERS
        if (type == CommandTargetType.SELF) assertIsPlayer(sender)
        val profile = if (type == CommandTargetType.SELF) getProfile(sender) else getProfile(args[0])
        messageFormatter.setTarget(profile)
        args = trimArray(args, if (type == CommandTargetType.SELF) 0 else 1)
        assertCommandPermission(sender, type.code())
        var nickname = joinArray(args)
        nickname = replaceColorAndFormat(nickname)
        messageFormatter.addFormatterPattern("nickname", nickname)
        if (type == CommandTargetType.OTHERS) if (!isTargetSelf(
                sender,
                profile.name
            ) && profile.player != null
        ) sendSilentMessage(
            profile.player!!, cmdMsg("target-message")
        )
        sendMessage(sender, cmdMsg(type.code()))
        nick(profile, nickname)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getProfileNames() else null

}