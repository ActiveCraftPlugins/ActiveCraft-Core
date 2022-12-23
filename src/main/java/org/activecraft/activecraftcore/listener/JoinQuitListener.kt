package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.manager.VanishManager.hideAll
import org.activecraft.activecraftcore.manager.VanishManager.setVanished
import org.activecraft.activecraftcore.messages.MessageFormatter
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.playermanagement.Playerlist
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.createIfNotExists
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.activecraft.activecraftcore.playermanagement.tables.ProfilesTable.writeToDatabase
import org.activecraft.activecraftcore.utils.config.MainConfig
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import java.time.LocalDateTime

class JoinQuitListener : Listener {
    var mainConfig: MainConfig = ActiveCraftCore.INSTANCE.mainConfig
    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerWorldChange(event: PlayerTeleportEvent) {
        val player = event.player
        val playerLocation = player.location
        val profile = of(player)
        profile.locationManager.setLastLocation(event.from.world, playerLocation)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val playerlist: Playerlist = ActiveCraftCore.INSTANCE.playerlist
        playerlist.addPlayerIfAbsent(player)
        val profile = createIfNotExists(player)
        val acCoreMessageSupplier: MessageSupplier = profile.getMessageSupplier(ActiveCraftCore.INSTANCE)!!
        profile.timesJoined = profile.timesJoined + 1
        playerlist.updateIfChanged(player)
        if (profile.locationManager.lastLocationBeforeQuit != null) player.teleport(profile.locationManager.lastLocationBeforeQuit!!)
        profile.displayManager.updateDisplayname()
        profile.effectManager.updateEffects()
        event.joinMessage = null
        val joinFormat = acCoreMessageSupplier.getFormatted(
            "general.join-format",
            MessageFormatter(acCoreMessageSupplier.activeCraftMessage)
                .addFormatterPattern("displayname", player.displayName)
                .addFormatterPattern("playername", player.name)
        )
        val sendJoinMessage: Boolean = mainConfig.sendJoinMessage

        // vanish stuff
        if (profile.isVanished) {
            setVanished(profile = profile, hide = true, silent = true)
            if (sendJoinMessage) Bukkit.broadcast(joinFormat, "activecraft.vanish.see")
        } else if (sendJoinMessage) {
            event.joinMessage = joinFormat
        }
        if (!player.hasPermission("activecraft.vanish.see")) hideAll(player)

        //fly
        if (profile.isFly) player.allowFlight = true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val playerLocation = player.location
        val profile = of(player)
        val acCoreMessageSupplier: MessageSupplier = profile.getMessageSupplier(ActiveCraftCore.INSTANCE)!!
        profile.lastOnline = LocalDateTime.now()
        profile.locationManager.setLastLocation(playerLocation.world, playerLocation, true)
        profile.bypassLockdown = player.hasPermission("lockdown.bypass")
        // TODO: 27.08.2022 save profile
        val before = System.nanoTime()
        writeToDatabase(profile)
        val after = System.nanoTime()
        println("Microseconds: " + (after - before) / 1000)
        event.quitMessage = null
        val quitFormat = acCoreMessageSupplier.getFormatted(
            "general.quit-format",
            MessageFormatter(acCoreMessageSupplier.activeCraftMessage)
                .addFormatterPattern("displayname", player.displayName)
                .addFormatterPattern("playername", player.name)
        )
        val sendQuitMsg: Boolean = mainConfig.sendQuitMessage
        if (profile.isVanished) {
            setVanished(profile = profile, hide = false, silent = true)
            if (sendQuitMsg) Bukkit.broadcast(quitFormat, "activecraft.vanish.see")
        } else if (sendQuitMsg) {
            event.quitMessage = quitFormat.replace("%displayname%", profile.nickname)
        }
    }
}