package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class EnderchestCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("enderchest", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        assertCommandPermission(sender, type.code())
        val player = getPlayer(sender)
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        messageFormatter.setTarget(getProfile(target))
        isTargetSelf(sender, target)
        player.openInventory(target.enderChest)
        player.playSound(target.location, Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f)
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}