package org.activecraft.activecraftcore.guis.profilemenu.inventory

import org.activecraft.activecraftcore.guicreator.GuiClickEvent
import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.push
import org.activecraft.activecraftcore.guis.effectgui.EffectGui
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.activecraft.activecraftcore.messages.ColorScheme
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.utils.ItemBuilder
import org.activecraft.activecraftcore.utils.shortInteger
import org.activecraft.activecraftcore.utils.toJadenCase
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import java.text.NumberFormat

class MainProfile(private val profileMenu: ProfileMenu) : GuiCreator(
    identifier = "main_profile",
    rows = 6,
    title = profileMenu.messageSupplier.getMessage(PREFIX + "title")
) {
    private val player: Player = profileMenu.player
    private val target: Player = profileMenu.target
    private val profile: Profile = profileMenu.profile
    private var connectionInfoStack: GuiItem? = null
    private var gameStats: GuiItem? = null
    private var violationStack: GuiItem? = null
    private var violationInfoStack: GuiItem? = null
    private var activeEffectsStack: GuiItem? = null
    private var gamemodeSwitcherStack: GuiItem? = null
    private var actionMenuStack: GuiItem? = null
    private var storageMenuStack: GuiItem? = null
    private var playerLocationStack: GuiItem? = null
    private var playtimeStack: GuiItem? = null
    private lateinit var activeEffectsBuilder: ItemBuilder
    private val messageSupplier: MessageSupplier = profileMenu.messageSupplier
    private val colorScheme: ColorScheme = profileMenu.colorScheme

    init {
        refresh()
    }

    override fun refresh() {
        // General Settings
        fillBackground(true)
        setItem(profileMenu.defaultGuiCloseItem, 49)
        setItem(profileMenu.playerHead, 4)
        val msgFormatter = PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage!!)
        val playtime = profile.playtime
        val playtimeMinutes = playtime % 60
        val playtimeHours = (playtime - playtimeMinutes) / 60
        msgFormatter
            .setTarget(profile)
            .addFormatterPatterns(
                Pair(
                    "ip",
                    target.address.hostName
                ),  // TODO: 22.08.2022 testen ob das klappt. wenn nicht dann wieder "target.getAddress().getAddress().toString().replace("/", "")"
                Pair("port", target.address.port.toString() + ""),
                Pair("ping", target.ping.toString() + ""),
                Pair("health", target.health.toString() + ""),
                Pair("food", target.foodLevel.toString() + ""),
                Pair("xp", target.level.toString() + ""),
                Pair("gamemode", toJadenCase(target.gameMode.name)),
                Pair("bans", profile.timesBanned.toString() + ""),
                Pair("ipbans", profile.timesIpBanned.toString() + ""),
                Pair("warns", profile.timesWarned.toString() + ""),
                Pair("mutes", profile.timesMuted.toString() + ""),
                Pair("hours", playtimeHours.toString() + ""),
                Pair("minutes", playtimeMinutes.toString() + "")
            )

        // Player Items
        val slotEmpty = GuiItem(Material.RED_STAINED_GLASS_PANE)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "empty-slot"))
        val offHand = GuiItem(target.inventory.itemInOffHand)
        val mainHand = GuiItem(target.inventory.itemInMainHand)
        setItem(if (mainHand.type != Material.AIR) mainHand else slotEmpty, 29)
        setItem(if (offHand.type != Material.AIR) offHand else slotEmpty, 20)
        setItem(if (target.inventory.helmet != null) GuiItem(target.inventory.helmet!!) else slotEmpty, 10)
        setItem(if (target.inventory.chestplate != null) GuiItem(target.inventory.chestplate!!) else slotEmpty, 19)
        setItem(if (target.inventory.leggings != null) GuiItem(target.inventory.leggings!!) else slotEmpty, 28)
        setItem(if (target.inventory.boots != null) GuiItem(target.inventory.boots!!) else slotEmpty, 37)

        // Player Connection Information
        connectionInfoStack = GuiItem(Material.STRUCTURE_VOID)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "connection.title"))
            .setLore(
                messageSupplier.getFormatted(PREFIX + "connection.ip", msgFormatter),
                messageSupplier.getFormatted(PREFIX + "connection.port", msgFormatter),
                messageSupplier.getFormatted(PREFIX + "connection.ping", msgFormatter)
            )
        setItem(connectionInfoStack, 21, player, "activecraft.connection.info")

        //player Stats
        val formatter = NumberFormat.getInstance()
        formatter.maximumFractionDigits = 2
        gameStats = GuiItem(Material.GRASS_BLOCK)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "player.title"))
            .setLore(
                messageSupplier.getFormatted(PREFIX + "player.health", msgFormatter),
                messageSupplier.getFormatted(PREFIX + "player.food", msgFormatter),
                messageSupplier.getFormatted(PREFIX + "player.exp", msgFormatter),
                messageSupplier.getFormatted(PREFIX + "player.gamemode", msgFormatter)
            )
        setItem(gameStats, 12, player, "activecraft.stats.info")

        // Player Violations Information
        violationInfoStack = GuiItem(Material.COMMAND_BLOCK)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "violations.title"))
            .setLore(
                messageSupplier.getFormatted(PREFIX + "violations.bans", msgFormatter),
                messageSupplier.getFormatted(PREFIX + "violations.ip-bans", msgFormatter),
                messageSupplier.getFormatted(PREFIX + "violations.warns", msgFormatter),
                messageSupplier.getFormatted(PREFIX + "violations.mutes", msgFormatter)
            )
        setItem(violationInfoStack, 30, player, "activecraft.violations.info")

        // active effects
        activeEffectsBuilder = ItemBuilder(Material.POTION)
            .displayname(messageSupplier.getMessage(PREFIX + "active-effects"))
        target.activePotionEffects.forEach { effect: PotionEffect ->
            activeEffectsBuilder.lore(
                messageSupplier.getMessage(
                    "effects." + effect.type.name.lowercase().replace("_", "-")
                )
                        + colorScheme.secondary + "; "
                        + colorScheme.secondaryAccent + effect.amplifier + colorScheme.secondary + "; "
                        + colorScheme.secondaryAccent + shortInteger(effect.duration / 20)
            )
        }

        activeEffectsStack = GuiItem(activeEffectsBuilder.build())
        val potionMeta = activeEffectsStack!!.itemMeta as PotionMeta
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
        activeEffectsStack!!.setItemMeta(potionMeta)
        // implement effectgui
        activeEffectsStack!!.addClickListener { guiClickEvent: GuiClickEvent? ->
            push(
                player,
                EffectGui(player, target).potionEffectGui.build()
            )
        }
        setItem(activeEffectsStack, 14, player, "activecraft.activeeffects")

        // Player Location Information
        // TODO: 22.08.2022 updaten
        playerLocationStack = GuiItem(Material.REDSTONE_TORCH)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "player-location"))
            .setLore(
                colorScheme.secondaryAccent.toString() + target.world.name + colorScheme.secondary + "; "
                        + colorScheme.secondaryAccent + target.location.blockX + colorScheme.secondary + ", "
                        + colorScheme.secondaryAccent + target.location.blockY + colorScheme.secondary + ", "
                        + colorScheme.secondaryAccent + target.location.blockZ
            )
        setItem(playerLocationStack, 16, player, "activecraft.location")

        //Player Playtime
        playtimeStack = GuiItem(Material.CLOCK)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "playtime"))
            .setLore(messageSupplier.getFormatted(PREFIX + "playtime-lore", msgFormatter))
        setItem(playtimeStack, 15, player, "activecraft.playtime")


        // Other Menus
        // Player Punishment
        violationStack = GuiItem(Material.COMMAND_BLOCK)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "violations-gui"))
            .addClickListener { guiClickEvent: GuiClickEvent? ->
                push(
                    player,
                    profileMenu.violationsProfile.build()
                )
            }
        setItem(violationStack, 42)

        // Player Gamemode Switcher
        gamemodeSwitcherStack = GuiItem(Material.GRASS_BLOCK)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "gamemode-switcher-gui"))
            .addClickListener { guiClickEvent: GuiClickEvent? ->
                push(
                    player,
                    profileMenu.gamemodeSwitcherProfile.build()
                )
            }
        setItem(gamemodeSwitcherStack, 32)

        // Player Storage Menu
        storageMenuStack = GuiItem(Material.CHEST)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "storage-gui"))
            .addClickListener { push(player, profileMenu.storageProfile.build()) }
        setItem(storageMenuStack, 33)

        // Player Actions Menu
        actionMenuStack = GuiItem(Material.LEVER)
            .setDisplayName(messageSupplier.getMessage(PREFIX + "action-gui"))
            .addClickListener { push(player, profileMenu.actionProfile.build()) }
        setItem(actionMenuStack, 34)
    }

    companion object {
        private const val PREFIX = "profile.mainprofile."
    }
}