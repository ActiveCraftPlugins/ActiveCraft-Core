package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidNumberException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

class BowCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("bow", plugin), Listener {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        when (args[0].lowercase()) {
            "explode" -> {
                val player = getPlayer(sender)
                assertCommandPermission(sender, "explode")
                val boombow = ItemStack(Material.BOW)
                val boombowmeta = boombow.itemMeta
                boombowmeta.setDisplayName(ChatColor.GOLD.toString() + "Boom Bow")
                boombowmeta.isUnbreakable = true
                if (args.size > 1) boombowmeta.lore =
                    listOf(ChatColor.AQUA.toString() + "Power: " + parseFloat(args[1]))
                boombow.itemMeta = boombowmeta
                boombow.addUnsafeEnchantment(Enchantment.WATER_WORKER, 124)
                boombow.addEnchantment(Enchantment.ARROW_INFINITE, 1)
                boombow.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                player.inventory.addItem(boombow)
            }

            "lightning" -> {
                val player = getPlayer(sender)
                assertCommandPermission(sender, "lightning")
                val lightningbow = ItemStack(Material.BOW)
                val lightningbowbowmeta = lightningbow.itemMeta
                lightningbowbowmeta.setDisplayName(ChatColor.GOLD.toString() + "Lightning Bow")
                lightningbowbowmeta.isUnbreakable = true
                lightningbow.itemMeta = lightningbowbowmeta
                lightningbow.addUnsafeEnchantment(Enchantment.WATER_WORKER, 124)
                lightningbow.addEnchantment(Enchantment.ARROW_INFINITE, 1)
                lightningbow.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                player.inventory.addItem(lightningbow)
            }
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        return if (args.size == 1) listOf("explode", "lightning") else null
    }

    @EventHandler
    fun onBowShot(event: ProjectileLaunchEvent) {
        val entity = event.entity
        val source = entity.shooter as? Player ?: return
        if (entity.type != EntityType.ARROW) return
        val item = source.inventory.itemInMainHand
        if (item.type != Material.BOW) return
        val itemMeta = item.itemMeta
        if (!item.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) return
        if (!itemMeta.hasEnchant(Enchantment.WATER_WORKER)) return
        if (itemMeta.getEnchantLevel(Enchantment.WATER_WORKER) != 124) return
        if (source.hasPermission("activecraft.bow.explode.trigger") && itemMeta.displayName.equals(
                ChatColor.GOLD.toString() + "Boom Bow",
                ignoreCase = true
            )
        ) {
            var power = 4f
            val lore = itemMeta.lore
            if (lore != null && lore.size > 0) {
                try {
                    power = parseFloat(lore[0].replace(ChatColor.AQUA.toString() + "Power: ", ""))
                } catch (ignored: InvalidNumberException) {
                }
            }
            projectiles[entity] = "e$power"
        }
        if (source.hasPermission("activecraft.bow.lightning.trigger") && itemMeta.displayName.equals(
                ChatColor.GOLD.toString() + "Lightning Bow",
                ignoreCase = true
            )
        ) {
            projectiles[entity] = "l"
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onProjectileHit(event: ProjectileHitEvent) {
        val entity = event.entity
        if (!projectiles.containsKey(entity)) return
        if (projectiles[entity]!!.startsWith("e")) entity.world.createExplosion(
            entity.location, projectiles[entity]!!
                .replace("e", "").toFloat(), false, true
        ) else if (projectiles[entity]!!.startsWith("l")) entity.world.strikeLightning(entity.location)
        entity.remove()
        projectiles.remove(entity)
    }

    companion object {
        private val projectiles: MutableMap<Projectile, String> = mutableMapOf()
    }
}