package org.activecraft.activecraftcore.guis.profilemenu.inventory

import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.push
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.activecraft.activecraftcore.guis.profilemenu.inventory.ReasonsProfile.ViolationType
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Material
import org.bukkit.entity.Player


class ViolationsProfile(private val profileMenu: ProfileMenu) : GuiCreator(
    identifier = "violations_profile",
    rows = 3,
    title = profileMenu.messageSupplier.getMessage(PREFIX + "title")
) {
    private val player: Player = profileMenu.player
    private val target: Player = profileMenu.target
    private val profile: Profile = profileMenu.profile
    private var warnStack: GuiItem? = null
    private var banStack: GuiItem? = null
    private var ipBanStack: GuiItem? = null
    private var muteStack: GuiItem? = null
    private var kickStack: GuiItem? = null
    private val messageSupplier: MessageSupplier = profileMenu.messageSupplier

    init {
        refresh()
    }

    override fun refresh() {
        setItem(profileMenu.defaultGuiCloseItem, 22)
        setItem(profileMenu.defaultGuiBackItem, 21)
        setItem(profileMenu.playerHead, 4)
        fillBackground(true)
        val msgFormatter = PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage!!)
        msgFormatter.setTarget(profile)
        setItem(GuiItem(Material.CHAIN_COMMAND_BLOCK)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "ban", msgFormatter))
            .addClickListener {
                push(
                    player,
                    ReasonsProfile(profileMenu, ViolationType.BAN).build()
                )
            }
            .also { banStack = it },
            14, player, "activecraft.ban"
        )
        setItem(GuiItem(Material.REDSTONE_BLOCK)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "warn", msgFormatter))
            .addClickListener {
                push(
                    player,
                    ReasonsProfile(profileMenu, ViolationType.WARN).build()
                )
            }
            .also { warnStack = it },
            11, player, "activecraft.warn.add"
        )
        muteStack = GuiItem(Material.NETHERITE_BLOCK)
            .addClickListener { player.performCommand((if (profile.isMuted) "unmute " else "mute ") + profileMenu.target.name) }
        if (!profile.isMuted) {
            muteStack!!.setDisplayName(messageSupplier.getFormatted(PREFIX + "mute", msgFormatter))
                .setLore(messageSupplier.getFormatted(PREFIX + "mute-lore", msgFormatter))
        } else {
            muteStack!!.setDisplayName(messageSupplier.getFormatted(PREFIX + "unmute", msgFormatter))
                .setLore(messageSupplier.getFormatted(PREFIX + "unmute-lore", msgFormatter))
        }
        setItem(muteStack, 12, player, "activecraft.mute")
        setItem(GuiItem(Material.REPEATING_COMMAND_BLOCK)
            .setDisplayName(
                messageSupplier.getFormatted(
                    PREFIX + "ban-ip",
                    msgFormatter.addFormatterPatterns(Pair("ip", target.address.hostName))
                )
            )
            .addClickListener {
                push(
                    player,
                    ReasonsProfile(profileMenu, ViolationType.BAN_IP).build()
                )
            }
            .also { ipBanStack = it },
            15, player, "activecraft.ban"
        )
        setItem(GuiItem(Material.COMMAND_BLOCK)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "kick", msgFormatter))
            .addClickListener {
                push(
                    player,
                    ReasonsProfile(profileMenu, ViolationType.KICK).build()
                )
            }
            .also { kickStack = it },
            13, player, "activecraft.kick"
        )
    }

    companion object {
        private const val PREFIX = "profile.violations-gui."
    }
}