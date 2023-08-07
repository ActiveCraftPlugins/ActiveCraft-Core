package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.activecraft.activecraftcore.utils.config.MainConfig
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.permissions.Permissible
import org.bukkit.permissions.PermissionAttachmentInfo

class DeathListener : Listener {

    companion object {
        val suiciders: MutableList<Player> = mutableListOf()
    }

    private fun hasEffectivePermission(permissible: Permissible, permission: String): Boolean {
        return permissible.effectivePermissions
            .map { obj: PermissionAttachmentInfo -> obj.permission }
            .any { anotherString: String? -> permission.equals(anotherString, ignoreCase = true) }
    }

    var mainConfig: MainConfig = ActiveCraftCore.INSTANCE.mainConfig
    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        val died = e.entity //in 1.17 gibt get player nen error deshalb IMMER get entity
        val profile = of(died)
        val killer = died.killer
        profile.locationManager.setLastLocation(died.world, died.location)
        if (hasEffectivePermission(died, "activecraft.keepexp")) {
            e.keepLevel = true
            e.setShouldDropExperience(false)
        }
        if (hasEffectivePermission(died, "activecraft.keepinventory")) {
            e.keepInventory = true
            e.drops.clear()
        }
        if (mainConfig.dropAllExp) {
            e.droppedExp = died.totalExperience
        }
        if (suiciders.contains(died)) {
            e.deathMessage = null
            suiciders.remove(died)
            return
        }
        if (profile.isVanished) {
            e.deathMessage = null
            return
        }
        var deathmessage = e.deathMessage
        if (killer != null && killer.inventory.itemInMainHand.type == Material.AIR) {
            val acCoreMessageSupplier: MessageSupplier = profile.getMessageSupplier(ActiveCraftCore.INSTANCE)!!
            val vanishTagFormat =
                acCoreMessageSupplier.getMessage("command.vanish.tag", acCoreMessageSupplier.colorScheme.secondary)
            val afkTagFormat =
                acCoreMessageSupplier.getMessage("command.afk.tag", acCoreMessageSupplier.colorScheme.secondary)
            var beforeBrackets = deathmessage!!.split("\\[".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0]
            beforeBrackets = beforeBrackets.replace(
                died.name, died.displayName
                    .replace(" $vanishTagFormat", "")
                    .replace(" $afkTagFormat", "")
                        + ChatColor.WHITE
            )
                .replace(
                    killer.name, killer.displayName
                        .replace(" $vanishTagFormat", "")
                        .replace(" $afkTagFormat", "")
                            + ChatColor.RESET
                )
            e.deathMessage =
                ChatColor.RED.toString() + "☠ " + beforeBrackets + killer.inventory.itemInMainHand.itemMeta.displayName
        } else {
            deathmessage = deathmessage!!.replace(died.name, died.displayName + ChatColor.RESET)
            e.deathMessage = ChatColor.RED.toString() + "☠ " + deathmessage
        }
    }
}