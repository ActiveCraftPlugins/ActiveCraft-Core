package org.activecraft.activecraftcore.guis.profilemenu.inventory

import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.bukkit.Material
import org.bukkit.entity.Player

class StorageProfile(private val profileMenu: ProfileMenu) : GuiCreator(
    identifier = "storage_profile",
    rows = 3,
    title = profileMenu.messageSupplier.getMessage(
        PREFIX + "title"
    )
) {
    private val player: Player = profileMenu.player
    private val target: Player = profileMenu.target
    private var invSeeStack: GuiItem? = null
    private var offInvStack: GuiItem? = null
    private var enderchestStack: GuiItem? = null
    private val messageSupplier: MessageSupplier = profileMenu.messageSupplier

    init {
        refresh()
    }

    override fun refresh() {
        fillBackground(true)
        setItem(profileMenu.defaultGuiCloseItem, 22)
        setItem(profileMenu.defaultGuiBackItem, 21)
        setItem(profileMenu.playerHead, 4)
        val msgFormatter = PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage!!)
        msgFormatter.setTarget(profileMenu.profile)
        invSeeStack = GuiItem(Material.CHEST)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "inventory", msgFormatter))
            .addClickListener { player.performCommand("invsee " + profileMenu.target.name) }
        setItem(invSeeStack, 12, player, "activecraft.invsee")
        enderchestStack = GuiItem(Material.ENDER_CHEST)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "enderchest", msgFormatter))
            .addClickListener { player.performCommand("enderchest " + profileMenu.target.name) }
        setItem(enderchestStack, 14, player, "activecraft.enderchest.others")
        offInvStack = GuiItem(Material.SHIELD)
            .setDisplayName(messageSupplier.getFormatted(PREFIX + "armorinv", msgFormatter))
            .addClickListener { player.performCommand("offinvsee " + profileMenu.target.name) }
        setItem(offInvStack, 13)
    }

    companion object {
        private const val PREFIX = "profile.storage-gui."
    }
}