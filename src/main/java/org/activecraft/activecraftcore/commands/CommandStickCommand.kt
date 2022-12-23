package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.removeColorAndFormat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

class CommandStickCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("commandstick", plugin), Listener {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        if (!isValidCommand(args[0])) {
            sendMessage(sender, getMessageSupplier().errors.invalidCommand)
            return
        }
        val commandStick = ItemStack(Material.STICK)
        val commandStickMeta = commandStick.itemMeta
        /*val tagCompound = NBTTagCompound()
        val nmsCommandStick = CraftItemStack.asNMSCopy(commandStick)
        nmsCommandStick.c(tagCompound)
        tagCompound.a("command", joinArray(args, 0))
        nmsCommandStick.b(tagCompound)*/ // TODO: zu dem hier ummodeln
        commandStickMeta.setDisplayName(ChatColor.GOLD.toString() + "Command Stick")
        commandStickMeta.isUnbreakable = true
        commandStickMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        commandStickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        val lore: MutableList<String> = ArrayList()
        lore.add(ChatColor.GOLD.toString() + "Bound Command: /" + ChatColor.AQUA + joinArray(args, 0))
        commandStickMeta.lore = lore
        commandStick.itemMeta = commandStickMeta
        player.inventory.addItem(commandStick)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onCommandStickInteract(event: PlayerInteractEntityEvent) {
        if (event.rightClicked.type != EntityType.PLAYER) return
        val player = event.player
        val target = event.rightClicked
        handleCommandStickEvent(player, player.inventory.itemInMainHand, target, event)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onCommandStickInteract(event: EntityDamageByEntityEvent) {
        if (event.damager.type != EntityType.PLAYER || event.entityType != EntityType.PLAYER) return
        val player = event.damager as Player
        val target = event.entity as Player
        handleCommandStickEvent(player, player.inventory.itemInMainHand, target, event)
    }

    @EventHandler
    fun onCommandStickInteract(event: PlayerInteractEvent) {
        if (event.action == Action.PHYSICAL) return
        handleCommandStickEvent(event.player, event.item, null, event)
    }

    private fun handleCommandStickEvent(player: Player, item: ItemStack?, target: Entity?, cancellable: Cancellable) {
        if (item == null) return
        if (item.type != Material.STICK) return
        val itemMeta = item.itemMeta
        if (itemMeta.displayName != "§6Command Stick") return
        if (!(itemMeta.itemFlags.contains(ItemFlag.HIDE_ENCHANTS) && itemMeta.itemFlags.contains(ItemFlag.HIDE_UNBREAKABLE))) return
        if (!itemMeta.isUnbreakable) return
        if (itemMeta.lore == null) return
        for (rawLore in itemMeta.lore!!) {
            var command = removeColorAndFormat(rawLore!!)
            command = command.replace("Bound Command: /", "")
            command = command.replace("/", "")
            target?.let { command = command.replace("@p", it.name) }
            player.performCommand(command)
        }
        cancellable.isCancelled = true
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (!(args.size == 1 && ActiveCraftCore.INSTANCE.mainConfig.hideCommandsAfterPluginName)) return null
        val pluginNames = listOf("minecraft", "bukkit", "spigot", "paper") +
                Bukkit.getPluginManager().plugins.map { it.name.lowercase() }
        return Bukkit.getCommandMap().knownCommands.keys
            .filter { cmd -> pluginNames.none { pluginName -> cmd.startsWith("$pluginName:") } }
    }
}