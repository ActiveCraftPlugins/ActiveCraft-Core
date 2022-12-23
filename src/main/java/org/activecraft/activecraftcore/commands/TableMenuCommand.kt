package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.guicreator.*
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.closeItemDisplayname
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.push
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TableMenuCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("tablemenu", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender, "tablemenu")
        val player = getPlayer(sender)
        val profile = getProfile(player)
        val guiCreator: GuiCreator =
            object : GuiCreator(
                identifier = "table_menu",
                rows = 6,
                title = activeCraftMessage.getMessage("tablemenu.title")
            ) {
                override fun refresh() {
                    setItem(GuiItem(Material.CRAFTING_TABLE)
                        .setDisplayName(msg("tablemenu.crafting-table"))
                        .addClickListener { player.performCommand("craftingtable") },
                        13
                    )
                    setItem(GuiItem(Material.CARTOGRAPHY_TABLE)
                        .setDisplayName(msg("tablemenu.cartography-table"))
                        .addClickListener { player.performCommand("cartographytable") },
                        20
                    )
                    setItem(GuiItem(Material.STONECUTTER)
                        .setDisplayName(msg("tablemenu.stonecutter"))
                        .addClickListener { player.performCommand("stonecutter") },
                        21
                    )
                    setItem(GuiItem(Material.ANVIL)
                        .setDisplayName(msg("tablemenu.anvil"))
                        .addClickListener { player.performCommand("anvil") }, 22
                    )
                    setItem(GuiItem(Material.GRINDSTONE)
                        .setDisplayName(msg("tablemenu.grindstone"))
                        .addClickListener { player.performCommand("grindstone") },
                        23
                    )
                    setItem(GuiItem(Material.LOOM)
                        .setDisplayName(msg("tablemenu.loom"))
                        .addClickListener { player.performCommand("loom") }, 24
                    )
                    setItem(GuiItem(Material.SMITHING_TABLE)
                        .setDisplayName(msg("tablemenu.smithing-table"))
                        .addClickListener { player.performCommand("smithingtable") },
                        31
                    )
                    fillBackground(true)
                    setItem(
                        GuiCloseItem(
                            displayName = closeItemDisplayname(profile.getMessageSupplier(GuiCreatorDefaults.acCoreMessage!!))
                        ), 49
                    )
                }
            }
        push(player, guiCreator.build())
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}