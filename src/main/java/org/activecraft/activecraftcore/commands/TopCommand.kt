package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.teleport
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TopCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("top", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        messageFormatter.setTarget(getProfile(target))
        assertCommandPermission(sender, type.code())
        val xBlock = target.location.blockX
        val zBlock = target.location.blockZ
        val x = target.location.x
        val z = target.location.z
        val loc = Location(
            target.world,
            x,
            target.world.getHighestBlockYAt(xBlock, zBlock).toDouble(),
            z,
            target.location.yaw,
            target.location.pitch
        )
        if (loc.block.type == Material.LAVA) {
            sendWarningMessage(sender, rawCmdMsg("not-safe"))
            return
        }
        loc.y = (loc.blockY + 1).toDouble()
        if (!isTargetSelf(sender, target)) sendSilentMessage(target, this.cmdMsg("target-message"))
        teleport(target, loc)
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}