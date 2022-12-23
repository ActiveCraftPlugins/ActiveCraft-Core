package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

class OpItemsCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("opitems", plugin!!) {
    class OpItem internal constructor(material: Material?, name: String, vararg c: Class?) : ItemStack(
        material!!
    ) {
        internal enum class Class {
            WEAPON, ARMOR, TOOL, NONE
        }

        init {
            val meta = itemMeta
            meta.setDisplayName("§r§b§kO§r §6$name §b§kO")
            meta.isUnbreakable = true
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            itemMeta = meta
            addEnchant(Enchantment.VANISHING_CURSE)
            addEnchant(Enchantment.MENDING)
            addEnchant(Enchantment.DURABILITY)
            for (cl in c) when (cl) {
                Class.ARMOR -> {
                    addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL)
                    addEnchant(Enchantment.PROTECTION_EXPLOSIONS)
                    addEnchant(Enchantment.PROTECTION_FIRE)
                    addEnchant(Enchantment.PROTECTION_PROJECTILE)
                }

                Class.WEAPON -> {
                    addEnchant(Enchantment.DAMAGE_ALL)
                    addEnchant(Enchantment.DAMAGE_ARTHROPODS)
                    addEnchant(Enchantment.DAMAGE_UNDEAD)
                }

                Class.TOOL -> {
                    addEnchant(Enchantment.LOOT_BONUS_BLOCKS)
                    addEnchant(Enchantment.DIG_SPEED)
                }

                else -> {}
            }
        }

        fun addEnchant(enchantment: Enchantment): OpItem {
            val meta = itemMeta
            meta.addEnchant(enchantment, 32767, true)
            itemMeta = meta
            return this
        }

        fun addEnchant(enchantment: Enchantment, i: Int): OpItem {
            val meta = itemMeta
            meta.addEnchant(enchantment, i, true)
            itemMeta = meta
            return this
        }
    }

    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = getPlayer(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        assertCommandPermission(sender)
        val sword = OpItem(Material.NETHERITE_SWORD, "OP Sword", OpItem.Class.WEAPON)
            .addEnchant(Enchantment.LOOT_BONUS_MOBS)
            .addEnchant(Enchantment.SWEEPING_EDGE)
            .addEnchant(Enchantment.FIRE_ASPECT)
        val axe = OpItem(Material.NETHERITE_AXE, "OP Axe", OpItem.Class.TOOL, OpItem.Class.WEAPON)
        val pickaxe = OpItem(Material.NETHERITE_PICKAXE, "OP Pickaxe", OpItem.Class.TOOL)
        val hoe = OpItem(Material.NETHERITE_HOE, "OP Hoe", OpItem.Class.NONE)
        val shovel = OpItem(Material.NETHERITE_SHOVEL, "OP Shovel", OpItem.Class.TOOL)
        val bow = OpItem(Material.BOW, "OP Bow", OpItem.Class.NONE)
            .addEnchant(Enchantment.ARROW_INFINITE)
            .addEnchant(Enchantment.ARROW_FIRE)
            .addEnchant(Enchantment.ARROW_DAMAGE)
        val crossbow = OpItem(Material.CROSSBOW, "OP Crossbow", OpItem.Class.NONE)
            .addEnchant(Enchantment.MULTISHOT)
            .addEnchant(Enchantment.QUICK_CHARGE, 5)
        val trident = OpItem(Material.TRIDENT, "OP Trident", OpItem.Class.NONE)
            .addEnchant(Enchantment.LOYALTY, 3)
            .addEnchant(Enchantment.IMPALING)
            .addEnchant(Enchantment.CHANNELING)
        val helmet = OpItem(Material.NETHERITE_HELMET, "OP Helmet", OpItem.Class.ARMOR)
            .addEnchant(Enchantment.WATER_WORKER)
            .addEnchant(Enchantment.OXYGEN)
        val chestplate = OpItem(Material.NETHERITE_CHESTPLATE, "OP Chestplate", OpItem.Class.ARMOR)
        val leggins = OpItem(Material.NETHERITE_LEGGINGS, "OP Leggins", OpItem.Class.ARMOR)
        val boots = OpItem(Material.NETHERITE_BOOTS, "OP Boots", OpItem.Class.ARMOR)
            .addEnchant(Enchantment.PROTECTION_FALL)
            .addEnchant(Enchantment.SOUL_SPEED, 5)
            .addEnchant(Enchantment.DEPTH_STRIDER)
        val elytra = OpItem(Material.ELYTRA, "OP Elytra", OpItem.Class.ARMOR)
        val itemList: Array<OpItem> = when (args[0].lowercase()) {
            "all" -> arrayOf(
                sword,
                bow,
                crossbow,
                trident,
                pickaxe,
                axe,
                shovel,
                hoe,
                helmet,
                chestplate,
                leggins,
                boots,
                elytra
            )

            "tools" -> arrayOf(pickaxe, axe, shovel, hoe)
            "armor" -> arrayOf(helmet, chestplate, leggins, boots, elytra)
            "weapons" -> arrayOf(sword, bow, crossbow, trident)
            "sword" -> arrayOf(sword)
            "bow" -> arrayOf(bow)
            "crossbow" -> arrayOf(crossbow)
            "trident" -> arrayOf(trident)
            "axe" -> arrayOf(axe)
            "pickaxe" -> arrayOf(pickaxe)
            "hoe" -> arrayOf(hoe)
            "shovel" -> arrayOf(shovel)
            "helmet" -> arrayOf(helmet)
            "chestplate" -> arrayOf(chestplate)
            "leggins" -> arrayOf(leggins)
            "boots" -> arrayOf(boots)
            "elytra" -> arrayOf(elytra)
            else -> arrayOf()
        }
        for (item in itemList) {
            player.inventory.addItem(item)
            player.playSound(player.location, Sound.valueOf("BLOCK_AMETHYST_BLOCK_BREAK"), 1f, 1f)
            try {
                Thread.sleep(25)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ) = listOf(
        "sword", "bow", "crossbow", "pickaxe", "axe", "shovel", "hoe", "helmet",
        "chestplate", "leggins", "boots", "tools", "tools", "armor", "weapons", "all"
    )

}