package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidEntityException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import java.util.*

class SummonCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("summon", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var args = args
        val isMultiple = args.size == 2 && Bukkit.getPlayer(args[0]) == null || args.size >= 3
        val type = when (args.size) {
            1 -> CommandTargetType.SELF
            2 -> if (isMultiple) CommandTargetType.SELF else CommandTargetType.OTHERS
            else -> CommandTargetType.OTHERS
        }
        var amount = 1
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        args = args.copyOfRange(if (type == CommandTargetType.OTHERS) 1 else 0, args.size)
        assertCommandPermission(sender, type.code() + if (isMultiple) ".multiple" else "")
        val entityType = parseEntityType(args[0])
        messageFormatter.setTarget(getProfile(target))
        messageFormatter.addFormatterPattern("mob", entityType.name)
        if (FORBIDDEN_ENTITY_TYPES.contains(entityType)) throw InvalidEntityException(args[0])
        if (isMultiple) {
            amount = parseInt(args[1])
            messageFormatter.addFormatterPattern("amount", amount.toString())
        }
        sendMessage(sender, this.cmdMsg(type.code() + if (isMultiple) "-multiple" else ""))
        for (i in 0 until amount) target.world.spawnEntity(target.location, entityType)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (args.size == 1) {
            return EntityType.values()
                .filter { !FORBIDDEN_ENTITY_TYPES.contains(it) }
                .map { it.name } + getBukkitPlayernames()
        } else if (args.size == 2 && Bukkit.getPlayer(args[0]) != null) {
            return EntityType.values()
                .filter { !FORBIDDEN_ENTITY_TYPES.contains(it) }
                .map { it.name }
        }
        return null
    }

    companion object {
        private val FORBIDDEN_ENTITY_TYPES = listOf(
            EntityType.UNKNOWN, EntityType.LEASH_HITCH, EntityType.PLAYER
        )
    }
}