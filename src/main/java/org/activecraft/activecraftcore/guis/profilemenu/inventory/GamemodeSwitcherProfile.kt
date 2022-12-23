package org.activecraft.activecraftcore.guis.profilemenu.inventory

import org.activecraft.activecraftcore.guicreator.GuiClickEvent
import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pushReplacement
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*


class GamemodeSwitcherProfile(private val profileMenu: ProfileMenu) : GuiCreator(
    identifier = "gamemode_switcher_profile",
    rows = 3,
    title = profileMenu.messageSupplier.getMessage(PREFIX + "title")
) {
    private val player: Player = profileMenu.player
    private val target: Player = profileMenu.target
    private val msgFormatter = PlayerMessageFormatter(activeCraftCoreMessage!!)
    private val messageSupplier: MessageSupplier = profileMenu.messageSupplier
    private var survivalStack: GuiItem? = null
    private var creativeStack: GuiItem? = null
    private var spectatorStack: GuiItem? = null
    private var adventureStack: GuiItem? = null
    private var currentGamemodeStack: GuiItem? = null

    init {
        refresh()
    }

    private fun sendCommand(gamemode: String) {
        player.performCommand(gamemode + " " + profileMenu.target.name)
    }

    private fun createGamemodeItem(gamemode: String, material: Material): GuiItem {
        return GuiItem(material)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + gamemode, msgFormatter))
            .addClickListener { guiClickEvent: GuiClickEvent ->
                sendCommand(gamemode)
                pushReplacement(player, guiClickEvent.gui!!.rebuild())
            }
    }

    private fun addGamemodeItem(gamemode: String, material: Material, slot: Int): GuiItem {
        val item = createGamemodeItem(gamemode, material)
        setItem(item, slot, player, "activecraft.gamemode.$gamemode.others")
        return item
    }

    override fun refresh() {
        msgFormatter.setTarget(profileMenu.profile)
        fillBackground(true)
        setItem(profileMenu.defaultGuiCloseItem, 21)
        setItem(profileMenu.defaultGuiBackItem, 22)
        setItem(profileMenu.playerHead, 4)
        creativeStack = addGamemodeItem("creative", Material.GRASS_BLOCK, 11)
        survivalStack = addGamemodeItem("survival", Material.IRON_SWORD, 12)
        setItem(
            GuiItem(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayName(
                    messageSupplier.getMessage(
                        PREFIX + "current-gamemode-" + target.gameMode.name.lowercase(
                            Locale.getDefault()
                        )
                    )
                ).also { currentGamemodeStack = it },
            13
        )
        adventureStack = addGamemodeItem("adventure", Material.MAP, 14)
        spectatorStack = addGamemodeItem("spectator", Material.ENDER_EYE, 15)
    }

    companion object {
        private const val PREFIX = "profile.gamemode-switcher-gui."
    }
}