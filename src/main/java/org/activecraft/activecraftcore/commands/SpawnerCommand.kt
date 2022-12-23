package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.CreatureSpawner
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import java.util.*

class SpawnerCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("spawner", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var args = args
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val type = if (args.size == 1) CommandTargetType.SELF else CommandTargetType.OTHERS
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        args = args.copyOfRange(if (type == CommandTargetType.OTHERS) 1 else 0, args.size)
        assertCommandPermission(sender, type.code())
        val mobName = parseEntityType(args[0]).name.uppercase(Locale.getDefault())
        val spawner = ItemStack(Material.SPAWNER)
        val spawnermeta = spawner.itemMeta as BlockStateMeta
        val spawnerblock = spawnermeta.blockState as CreatureSpawner
        spawnerblock.spawnedType = EntityType.valueOf(mobName)
        messageFormatter.addFormatterPattern("spawner", mobName.lowercase().replace("_", " "))
        messageFormatter.setTarget(getProfile(target))
        spawnermeta.setDisplayName(this.cmdMsg("displayname"))
        spawnermeta.blockState = spawnerblock
        spawner.itemMeta = spawnermeta
        if (type == CommandTargetType.OTHERS) if (!isTargetSelf(sender, target)) sendSilentMessage(
            target,
            this.cmdMsg("target-message")
        )
        sendMessage(sender, this.cmdMsg(type.code()))
        target.inventory.addItem(spawner)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        val entityNames= EntityType.values()
            .map { obj: EntityType -> obj.name }
            .filter { !it.equals("UNKNOWN", ignoreCase = true) }
        if (args.size == 1) {
            return entityNames + getBukkitPlayernames()
        } else if (args.size == 2 && Bukkit.getPlayer(args[0]) != null) {
            return entityNames
        }
        return null
    }
}