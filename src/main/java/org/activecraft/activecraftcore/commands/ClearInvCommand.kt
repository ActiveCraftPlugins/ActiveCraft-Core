package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ClearInvCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("clearinventory", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        messageFormatter.setTarget(getProfile(target))
        assertCommandPermission(sender, type.code())
        if (type == CommandTargetType.OTHERS) {
            if (!isTargetSelf(sender, target)) {
                sendSilentMessage(target, this.cmdMsg("target-message"))
            }
        }
        target.inventory.clear()
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}