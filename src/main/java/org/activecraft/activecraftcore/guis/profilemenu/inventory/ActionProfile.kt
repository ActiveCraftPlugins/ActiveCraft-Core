package org.activecraft.activecraftcore.guis.profilemenu.inventory

import org.activecraft.activecraftcore.guicreator.GuiClickEvent
import org.activecraft.activecraftcore.guicreator.GuiConfirmation
import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.push
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pushReplacement
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.activecraft.activecraftcore.messages.ColorScheme
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.PotionMeta

class ActionProfile(private val profileMenu: ProfileMenu) : GuiCreator(
    identifier = "action_profile",
    rows = 6,
    title = profileMenu.messageSupplier.getMessage(PREFIX + "title")
) {
    private val player: Player = profileMenu.player
    private val target: Player = profileMenu.target
    private val profile: Profile = profileMenu.profile
    private val messageSupplier: MessageSupplier = profileMenu.messageSupplier
    private val colorScheme: ColorScheme = profileMenu.colorScheme
    private var tpToPlayerItem: GuiItem? = null
    private var tpherePlayerItem: GuiItem? = null
    private var clearInvItem: GuiItem? = null
    private var flyItem: GuiItem? = null
    private var godModeItem: GuiItem? = null
    private var vanishItem: GuiItem? = null
    private var feedItem: GuiItem? = null
    private var healItem: GuiItem? = null
    private var homeItem: GuiItem? = null
    private var strikeItem: GuiItem? = null
    private var killItem: GuiItem? = null
    private var explodeItem: GuiItem? = null

    init {
        refresh()
    }

    override fun refresh() {
        val cmdMsgFormatter = PlayerMessageFormatter(activeCraftCoreMessage!!)
        cmdMsgFormatter.setTarget(profile)
        fillBackground(true)
        setItem(profileMenu.defaultGuiCloseItem, 49)
        setItem(profileMenu.defaultGuiBackItem, 38) // TODO: 27.08.2022 wirklich 38 und nicht 48?
        setItem(profileMenu.playerHead, 4)
        setItem(GuiItem(Material.ENCHANTED_GOLDEN_APPLE)
            .setDisplayName(
                messageSupplier.getFormatted(
                    PREFIX + "god." + (if (profile.isGodmode) "dis" else "en") + "able",
                    cmdMsgFormatter
                )
            )
            .setLore(
                messageSupplier.getFormatted(
                    PREFIX + "god." + (if (profile.isGodmode) "dis" else "en") + "able-lore",
                    cmdMsgFormatter
                )
            )
            .addClickListener { guiClickEvent: GuiClickEvent ->
                player.performCommand("god " + profileMenu.target.name)
                pushReplacement(player, guiClickEvent.gui!!.rebuild())
            }.also { godModeItem = it }, 10, player, "activecraft.god.others"
        )
        setItem(GuiItem(Material.COOKED_BEEF)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "feed", cmdMsgFormatter))
            .addClickListener { guiClickEvent: GuiClickEvent? -> player.performCommand("feed " + profileMenu.target.name) }
            .also { feedItem = it },
            12, player, "activecraft.feed.others"
        )
        setItem(GuiItem(Material.RED_BED)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "homes", cmdMsgFormatter))
            .addClickListener { guiClickEvent: GuiClickEvent? ->
                push(
                    player,
                    profileMenu.homeListPageLayout.guiPage.build()
                )
            }
            .also { homeItem = it },
            14, player, "activecraft.home.others"
        )
        setItem(GuiItem(Material.LIGHTNING_ROD)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "strike", cmdMsgFormatter))
            .addClickListener { player.performCommand("strike " + profileMenu.target.name) }
            .also { strikeItem = it },
            16, player, "activecraft.strike.others"
        )
        setItem(GuiItem(Material.FEATHER)
            .setDisplayName(
                messageSupplier.getFormatted(
                    PREFIX + "fly." + if (profile.isFly) "disable" else "enable",
                    cmdMsgFormatter
                )
            )
            .setLore(
                messageSupplier.getFormatted(
                    PREFIX + "fly." + (if (profile.isFly) "dis" else "en") + "able-lore",
                    cmdMsgFormatter
                )
            )
            .addClickListener { guiClickEvent: GuiClickEvent ->
                player.performCommand("fly " + profileMenu.target.name)
                pushReplacement(player, guiClickEvent.gui!!.rebuild())
            }.also { flyItem = it },
            19, player, "activecraft.fly.others"
        )
        healItem = GuiItem(Material.POTION)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "heal", cmdMsgFormatter))
            .addClickListener { player.performCommand("heal " + profileMenu.target.name) }
        val potionMeta = healItem!!.itemMeta as PotionMeta
        potionMeta.color = Color.FUCHSIA
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
        healItem!!.setItemMeta(potionMeta)
        setItem(healItem, 21, player, "activecraft.heal.others")
        setItem(GuiItem(Material.ENDER_PEARL)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "teleport-there", cmdMsgFormatter))
            .addClickListener {
                push(player, GuiConfirmation("action_profile.tp_to")
                    .performAfterConfirm { player.performCommand("tp " + profileMenu.target.name) }
                    .build())
            }.also { tpToPlayerItem = it },
            23, player, "activecraft.tp"
        )
        setItem(GuiItem(Material.TNT)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "explode", cmdMsgFormatter))
            .addClickListener { player.performCommand("explode " + profileMenu.target.name) }
            .also { explodeItem = it }, 25, player, "activecraft.explode.others"
        )
        setItem(GuiItem(Material.GLASS_BOTTLE)
            .setDisplayName(
                messageSupplier.getFormatted(
                    PREFIX + "vanish." + (if (profile.isVanished) "un" else "") + "vanish",
                    cmdMsgFormatter
                )
            )
            .setLore(
                messageSupplier.getFormatted(
                    PREFIX + "vanish." + (if (profile.isVanished) "un" else "") + "vanish-lore",
                    cmdMsgFormatter
                )
            )
            .addClickListener { guiClickEvent: GuiClickEvent ->
                player.performCommand("vanish " + profileMenu.target.name)
                pushReplacement(player, guiClickEvent.gui!!.rebuild())
            }.also { vanishItem = it }, 28, player, "activecraft.vanish.others"
        )
        setItem(GuiItem(Material.STRUCTURE_VOID)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "clear", cmdMsgFormatter))
            .addClickListener {
                push(player, GuiConfirmation("action_profile.clearinv")
                    .performAfterConfirm { player.performCommand("clear " + profileMenu.target.name) }
                    .build())
            }.also { clearInvItem = it },
            30, player, "activecraft.clearinv"
        )
        setItem(GuiItem(Material.ENDER_EYE)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "teleport-here", cmdMsgFormatter))
            .addClickListener {
                push(player, GuiConfirmation("action_profile.tphere")
                    .performAfterConfirm { player.performCommand("tphere " + profileMenu.target.name) }
                    .build())
            }.also { tpherePlayerItem = it },
            32, player, "activecraft.tphere"
        )
        setItem(GuiItem(Material.SKELETON_SKULL)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "kill", cmdMsgFormatter))
            .addClickListener {
                push(player, GuiConfirmation("action_profile.kill")
                    .performAfterConfirm { player.performCommand("kill " + profileMenu.target.name) }
                    .build())
            }.also { killItem = it },
            34, player, "activecraft.kill"
        )
    }

    companion object {
        private const val PREFIX = "profile.action-gui."
    }
}