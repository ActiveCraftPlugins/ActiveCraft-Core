package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.playermanagement.Profilev2.Companion.of
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class SignInteractListener : Listener {
    fun log(text: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED.toString() + text)
    }

    @EventHandler
    fun onSignClick(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.clickedBlock == null) return
        val player = event.player
        val blockState = event.clickedBlock!!.state
        if (blockState is Sign) {
            if (player.isSneaking) {
                val profile = of(player)
                if (profile.editSign) {
                    event.isCancelled = true
                    val signBlock = event.clickedBlock!!.state as Sign
                    player.openSign(signBlock)
                }
            }
        }
    }
}