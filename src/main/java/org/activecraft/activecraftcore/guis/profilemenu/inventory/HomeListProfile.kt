package org.activecraft.activecraftcore.guis.profilemenu.inventory

import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.playermanagement.WarnManager
import org.bukkit.Material
import org.bukkit.entity.Player

class HomeListProfile( // TODO: 22.08.2022 wahrscheinlich datei löschen weil wir jetzt PageLayout benutzen
    private val profileMenu: ProfileMenu
) {
    private val player: Player = profileMenu.player
    private val target: Player = profileMenu.target
    private val profile: Profile = profileMenu.profile
    private val warnManager: WarnManager? = null
    private val playerHead: GuiPlayerHead? = null
    private val nextArrow: GuiItem? = null
    private val prevArrow: GuiItem? = null

    private var currentPage = 0
    private val pages: MutableList<GuiCreator>

    init {
        pages = ArrayList()
        renew()
    }

    private class Page(messageSupplier: MessageSupplier) : GuiCreator(
        identifier = "home_list_profile",
        rows = 6,
        title = messageSupplier.getMessage(PREFIX + "title")
    ) {
        override fun refresh() {}
    }

    fun renew() {
        currentPage = 0
        pages.clear()
        val msgFormatter = PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage!!).setTarget(profile)
        val homes = profile.homeManager.homes.toMutableMap()
        if (homes.isEmpty()) {
            pages.add(
                Page(profileMenu.messageSupplier)
                    .fillBackground(true)
                    .setItem(profileMenu.defaultGuiCloseItem, 49)
                    .setItem(profileMenu.defaultGuiBackItem, 48)
                    .setItem(profileMenu.playerHead, 4)
                    .setItem(
                        GuiItem(Material.BARRIER).setDisplayName(
                            profileMenu.messageSupplier.getFormatted(PREFIX + "no-homes", msgFormatter)
                        ), 22
                    )
            )
        }
        while (homes.isNotEmpty()) {
            val toBeRemoved: MutableList<String> = ArrayList()
            val page = Page(
                profileMenu.messageSupplier
            )
            page.fillBackground(true)
                .setItem(profileMenu.defaultGuiCloseItem, 49)
                .setItem(profileMenu.defaultGuiBackItem, 48)
                .setItem(profileMenu.playerHead, 4)
            var i = 10
            for (homeName in homes.keys) {
                val loc = homes[homeName]!!.location
                val environment = loc.world.environment
                page.setItem(
                    GuiItem(
                        when (environment.id) {
                            1 -> Material.END_STONE
                            -1 -> Material.NETHERRACK
                            else -> Material.GRASS_BLOCK
                        }
                    )
                        .setDisplayName(homeName)
                        .setLore()
                        .addClickListener { player.performCommand("home " + target.name + " " + homeName) },
                    i
                )
                toBeRemoved.add(homeName)
                if (i % 9 != 7 && i % 9 != 0) i++ else i += 3
                if (i >= 44) break
            }
            for (s in toBeRemoved) homes.remove(s)
            pages.add(page)
        }

        if (pages.size == 2) {
            pages[0].setItem(nextArrow, 53)
            pages[1].setItem(prevArrow, 45)
        } else if (pages.size >= 3) {
            for (pos in pages.indices) {
                when (pos) {
                    0 -> {
                        pages[pos].setItem(nextArrow, 53)
                    }
                    pages.size - 1 -> {
                        pages[pos].setItem(prevArrow, 45)
                    }
                    else -> {
                        pages[pos].setItem(nextArrow, 53)
                        pages[pos].setItem(prevArrow, 45)
                    }
                }
            }
        }
    }

    fun getPage(index: Int): GuiCreator {
        return pages[index]
    }

    companion object {
        private const val PREFIX = "profile.homelist."
    }
}