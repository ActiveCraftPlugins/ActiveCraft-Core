package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.PlayerWarpEvent
import org.activecraft.activecraftcore.events.WarpCreateEvent
import org.activecraft.activecraftcore.events.WarpDeleteEvent
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

object WarpManager {
    @JvmStatic
    fun getWarp(name: String) = ActiveCraftCore.INSTANCE.warpsConfig.warps[name]

    @JvmStatic
    fun createWarp(name: String, location: Location) {
        //call event
        val event = WarpCreateEvent(location, name)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return

        //add permissions
        val childMap: MutableMap<String, Boolean> = HashMap()
        childMap["activecraft.warp.self"] = true
        childMap["activecraft.warp"] = true
        Bukkit.getPluginManager().addPermission(
            Permission(
                "activecraft.warp.self.$name",
                "Permission to warp yourself to a specific warp.", PermissionDefault.OP, childMap
            )
        )
        childMap.clear()
        childMap["activecraft.warp.others"] = true
        childMap["activecraft.warp"] = true
        Bukkit.getPluginManager().addPermission(
            Permission(
                "activecraft.warp.others.$name",
                "Permission to warp another player to a specific warp.", PermissionDefault.OP, childMap
            )
        )

        //add warp to config
        ActiveCraftCore.INSTANCE.warpsConfig.set(event.warpName, event.location, true)
    }

    @JvmStatic
    fun deleteWarp(name: String) {
        //call event
        val event = WarpDeleteEvent(getWarp(name) ?: return, name)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return

        //remove permissions
        Bukkit.getPluginManager().removePermission("activecraft.warp.self.$name")
        Bukkit.getPluginManager().removePermission("activecraft.warp.others.$name")

        //add warp to config
        ActiveCraftCore.INSTANCE.warpsConfig.set(event.warpName, null, true)
    }

    @JvmStatic
    fun warp(player: Player, warpName: String) {
        //call event
        val event = PlayerWarpEvent(of(player), getWarp(warpName) ?: return, warpName!!)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return
        //teleport
        player.teleport(event.location)
        player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
    }
}