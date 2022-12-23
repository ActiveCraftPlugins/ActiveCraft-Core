package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.teleport
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class RandomTPCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("randomtp", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var args = args
        val isLimited = args.size == 1 && isInt(args[0]) || args.size >= 2
        val type = when (args.size) {
            0 -> CommandTargetType.SELF
            1 -> if (isLimited) CommandTargetType.SELF else CommandTargetType.OTHERS
            else -> CommandTargetType.OTHERS
        }
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        args = args.copyOfRange(if (type == CommandTargetType.OTHERS) 1 else 0, args.size)
        assertCommandPermission(sender, type.code())
        var range = target.world.worldBorder.size.toInt() / 2
        if (isLimited) range = parseInt(args[0])
        var tpLoc = randomLocation(target, range)
        val world = tpLoc.world
        for (i in 0..69419) {
            val block = world.getBlockAt(
                tpLoc.blockX,
                world.getHighestBlockYAt(tpLoc.blockX, tpLoc.blockZ),
                tpLoc.blockZ
            )
            if (block.type != Material.LAVA) break
            tpLoc = randomLocation(target, range)
        }
        teleport(target, tpLoc)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null


    private fun randomLocation(player: Player, range: Int): Location {
        val random = Random()
        var randomNumX = random.nextInt(range)
        var randomNumZ = random.nextInt(range)
        when (random.nextInt(4)) {
            1 -> randomNumX *= -1
            2 -> randomNumZ *= -1
            3 -> {
                randomNumX *= -1
                randomNumZ *= -1
            }
        }
        return Location(
            player.world,
            randomNumX.toDouble(),
            (player.world.getHighestBlockYAt(randomNumX, randomNumZ) + 1).toDouble(),
            randomNumZ.toDouble(),
            player.location.yaw,
            player.location.pitch
        )
    }
}