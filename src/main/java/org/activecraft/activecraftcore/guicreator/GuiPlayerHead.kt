package org.activecraft.activecraftcore.guicreator

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.meta.SkullMeta

class GuiPlayerHead(owningPlayer: OfflinePlayer) : GuiItem(Material.PLAYER_HEAD) {
    init {
        setOwner(owningPlayer)
    }

    fun setOwner(player: OfflinePlayer): GuiPlayerHead {
        val skullMeta = this.itemMeta as SkullMeta
        skullMeta.owningPlayer = player
        this.itemMeta = skullMeta
        return this
    }
}