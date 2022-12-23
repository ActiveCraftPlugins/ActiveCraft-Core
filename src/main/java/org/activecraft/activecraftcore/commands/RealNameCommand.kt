package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.removeColorAndFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class RealNameCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("realname", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val displayname = joinArray(args, 0).trim { it <= ' ' }
        messageFormatter.addFormatterPatterns(
            "nickname" to displayname,
            "players" to joinList(ActiveCraftCore.INSTANCE.profiles.values.filter {
                displayname.equals(removeColorAndFormat(it.nickname), ignoreCase = true)
            }.map { it.name }, 0, ", ")
        )
        sendMessage(sender, this.cmdMsg("header"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) Bukkit.getOnlinePlayers().map { Profile.of(it).rawNickname } else null

}