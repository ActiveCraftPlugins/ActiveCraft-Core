package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import java.util.*

class EnchantCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("enchant", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        assertArgsLength(args, ComparisonType.NOT_EQUAL, 0)
        if (player.inventory.itemInMainHand.type == Material.AIR) throw NotHoldingItemException(
            player,
            NotHoldingItemException.ExpectedItem.ANY
        )
        val armorMaterials = arrayOf(
            Material.LEATHER_BOOTS,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_HELMET,
            Material.IRON_BOOTS,
            Material.IRON_LEGGINGS,
            Material.IRON_CHESTPLATE,
            Material.IRON_HELMET,
            Material.CHAINMAIL_BOOTS,
            Material.CHAINMAIL_LEGGINGS,
            Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_HELMET,
            Material.GOLDEN_BOOTS,
            Material.GOLDEN_LEGGINGS,
            Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_HELMET,
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_HELMET,
            Material.NETHERITE_BOOTS,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_HELMET,
            Material.ELYTRA,
            Material.CARVED_PUMPKIN,
            Material.DRAGON_HEAD,
            Material.CREEPER_HEAD,
            Material.SKELETON_SKULL,
            Material.WITHER_SKELETON_SKULL,
            Material.PLAYER_HEAD,
            Material.ZOMBIE_HEAD
        )
        messageFormatter.addFormatterPattern("enchantment", args[0].lowercase())
        when (args[0].lowercase()) {
            "clear" -> {
                if (player.inventory.itemInMainHand.enchantments.isNotEmpty()) {
                    sendWarningMessage(sender, rawCmdMsg("not-enchanted"))
                    return
                }
                Enchantment.values()
                    .filter { player.inventory.itemInMainHand.containsEnchantment(it) }
                    .forEach { player.inventory.itemInMainHand.removeEnchantment(it) }
                sendMessage(sender, this.cmdMsg("cleared-all-enchantments"))
                player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
            }

            "glint" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                if (args[1].equals("true", ignoreCase = true)) {
                    val eitem = player.inventory.itemInMainHand
                    eitem.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1)
                    eitem.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    sendMessage(sender, this.cmdMsg("glint-true"))
                    player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
                } else if (args[1].equals("false", ignoreCase = true)) {
                    val eitem = player.inventory.itemInMainHand
                    eitem.removeEnchantment(Enchantment.WATER_WORKER)
                    eitem.removeItemFlags(ItemFlag.HIDE_ENCHANTS)
                    sendMessage(sender, this.cmdMsg("glint-false"))
                    player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
                } else sendMessage(sender, getMessageSupplier().errors.noTrueFalse)
            }

            "vanishing_curse" -> {
                val item = player.inventory.itemInMainHand
                item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1)
                sendMessage(sender, this.cmdMsg("applied-enchantment"))
                player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
            }

            "binding_curse" -> {
                val item = player.inventory.itemInMainHand
                if (armorMaterials.contains(item.type)) {
                    sendWarningMessage(sender, rawCmdMsg("cannot-be-applied"))
                    return
                }
                item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1)
                sendMessage(sender, this.cmdMsg("applied-enchantment"))
                player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
            }

            else -> {
                val enchantment =
                    Enchantment.getByKey(NamespacedKey("minecraft", args[0].lowercase()))
                        ?: throw InvalidArgumentException()
                val item = player.inventory.itemInMainHand
                val maxlvl = if (args.size == 1) enchantment.maxLevel else parseInt(args[1])
                item.addUnsafeEnchantment(enchantment, maxlvl)
                messageFormatter.addFormatterPattern("maxlevel", maxlvl.toString() + "")
                sendMessage(sender, this.cmdMsg("applied-enchantment"))
                player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
            }
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (args.size == 1) {
            return Enchantment.values().map { it.key }.map { it.key } + listOf("clear", "glint")
        } else if (args.size == 2 && args[0].equals("glint", ignoreCase = true)) {
            return listOf("true", "false")
        }
        return null
    }

    companion object {
        private val enchantmentMap = HashMap<String, Enchantment>()

        init {
            enchantmentMap["aqua_affinity"] = Enchantment.WATER_WORKER
            enchantmentMap["bane_of_arthropods"] = Enchantment.DAMAGE_ARTHROPODS
            enchantmentMap["blast_protection"] = Enchantment.PROTECTION_EXPLOSIONS
            enchantmentMap["channeling"] = Enchantment.CHANNELING
            enchantmentMap["depth_strider"] = Enchantment.DEPTH_STRIDER
            enchantmentMap["efficiency"] = Enchantment.DIG_SPEED
            enchantmentMap["feather_falling"] = Enchantment.PROTECTION_FALL
            enchantmentMap["fire_aspect"] = Enchantment.FIRE_ASPECT
            enchantmentMap["fire_protection"] = Enchantment.PROTECTION_FIRE
            enchantmentMap["flame"] = Enchantment.ARROW_FIRE
            enchantmentMap["fortune"] = Enchantment.LOOT_BONUS_BLOCKS
            enchantmentMap["frost_walker"] = Enchantment.FROST_WALKER
            enchantmentMap["impaling"] = Enchantment.IMPALING
            enchantmentMap["infinity"] = Enchantment.ARROW_INFINITE
            enchantmentMap["knockback"] = Enchantment.KNOCKBACK
            enchantmentMap["looting"] = Enchantment.LOOT_BONUS_MOBS
            enchantmentMap["loyalty"] = Enchantment.LOYALTY
            enchantmentMap["luck_of_the_sea"] = Enchantment.LUCK
            enchantmentMap["lure"] = Enchantment.LURE
            enchantmentMap["mending"] = Enchantment.MENDING
            enchantmentMap["multishot"] = Enchantment.MULTISHOT
            enchantmentMap["piercing"] = Enchantment.PIERCING
            enchantmentMap["power"] = Enchantment.ARROW_DAMAGE
            enchantmentMap["projectile_protection"] = Enchantment.PROTECTION_PROJECTILE
            enchantmentMap["punch"] = Enchantment.PROTECTION_ENVIRONMENTAL
            enchantmentMap["quick_charge"] = Enchantment.ARROW_KNOCKBACK
            enchantmentMap["respiration"] = Enchantment.QUICK_CHARGE
            enchantmentMap["riptide"] = Enchantment.OXYGEN
            enchantmentMap["protection"] = Enchantment.RIPTIDE
            enchantmentMap["sharpness"] = Enchantment.DAMAGE_ALL
            enchantmentMap["silk_touch"] = Enchantment.SILK_TOUCH
            enchantmentMap["smite"] = Enchantment.DAMAGE_UNDEAD
            enchantmentMap["soul_speed"] = Enchantment.SOUL_SPEED
            enchantmentMap["sweeping_edge"] = Enchantment.SWEEPING_EDGE
            enchantmentMap["thorns"] = Enchantment.THORNS
            enchantmentMap["unbreaking"] = Enchantment.DURABILITY
        }
    }
}