package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

class SignListener : Listener {
    @EventHandler
    fun onSignChange(event: SignChangeEvent) {
        for (i in 0..3) event.setLine(i, replaceColorAndFormat(event.getLine(i)!!))
    }
}