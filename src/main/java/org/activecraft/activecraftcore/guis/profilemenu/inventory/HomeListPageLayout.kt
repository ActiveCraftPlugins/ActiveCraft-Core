package org.activecraft.activecraftcore.guis.profilemenu.inventory

import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiPageLayout
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.activecraft.activecraftcore.messages.ColorScheme
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Material
import org.bukkit.entity.Player

class HomeListPageLayout(private val profileMenu: ProfileMenu) : GuiPageLayout(
    identifier = "home_list_profile",
    rows = 6,
    title = profileMenu.messageSupplier.getMessage(PREFIX + "title")
) {
    private val player: Player = profileMenu.player
    private val target: Player = profileMenu.target
    private val profile: Profile = profileMenu.profile
    private val colorScheme: ColorScheme = profileMenu.colorScheme
    override val maxPages: Int
        get() = chunkedHomeList.size

    val chunkedHomeList: List<MutableList<String>>
        get() {
            val chunked: MutableList<MutableList<String>> = ArrayList()
            val chunkSize = 35
            val keyList: MutableList<String> = profile.homeManager.homes.keys.toMutableList()
            var i = 0
            while (keyList.size > 0) {
                for (j in 0 until chunkSize) {
                    if (keyList.size == 0) break
                    if (j == 0) chunked.add(ArrayList())
                    chunked[i].add(keyList[0])
                    keyList.removeAt(0)
                }
                i++
            }
            return chunked
        }

    override fun refreshPage() {
        val chunkedHomeList: List<String> = chunkedHomeList[currentPage]
        val msgFormatter = PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage!!).setTarget(profile)
        guiPage.setItem(profileMenu.defaultGuiCloseItem, 49)
            .setItem(profileMenu.playerHead, 4)
            .setItem(profileMenu.defaultGuiBackItem, 48)
            .fillBackground(true)
        if (chunkedHomeList.isEmpty()) {
            guiPage.setItem(
                GuiItem(Material.BARRIER).setDisplayName(
                    profile.getMessageSupplier(
                        GuiCreatorDefaults.acCoreMessage
                    )
                        .getFormatted(PREFIX + "no-homes", msgFormatter)
                ), 22
            )
            return
        }
        var i = 10
        for (homeName in chunkedHomeList) {
            val loc = profile.homeManager.homes[homeName]!!.location
            val environment = loc.world.environment
            guiPage.setItem(
                GuiItem(
                    when (environment.id) {
                        1 -> Material.END_STONE
                        -1 -> Material.NETHERRACK
                        else -> Material.GRASS_BLOCK
                    }
                )
                    .setDisplayName(homeName)
                    .setLore(
                        colorScheme.primaryAccent.toString() + loc.world.name + colorScheme.primary
                                + ", " + colorScheme.primaryAccent + loc.blockX + colorScheme.primary
                                + ", " + colorScheme.primaryAccent + loc.blockY + colorScheme.primary
                                + ", " + colorScheme.primaryAccent + loc.blockZ
                    )
                    .addClickListener { player.performCommand("home " + target.name + " " + homeName) },
                i
            )
            if (i % 9 != 7 && i % 9 != 0) i++ else i += 3
            if (i >= 44) break
        }
    }

    companion object {
        private const val PREFIX = "profile.homelist."
    }
}