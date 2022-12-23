package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball

class FireBallCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("fireball", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        val power = if (args.isEmpty()) DEFAULT_POWER else parseFloat(args[0])
        val fire = if (args.size >= 2) parseBoolean(args[1]) else DEFAULT_FIRE // TODO: Boolean.parse oder ähnliches in commands zu parseBoolean
        val fireball = player.world.spawnEntity(player.location, EntityType.FIREBALL) as Fireball
        fireball.yield = power
        fireball.setIsIncendiary(fire)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 2) listOf("true", "false") else null


    companion object {
        private const val DEFAULT_POWER = 4f
        private const val DEFAULT_FIRE = true
    }
}