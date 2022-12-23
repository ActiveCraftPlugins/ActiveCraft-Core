package org.activecraft.activecraftcore.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class ActiveCraftEvent @JvmOverloads constructor(async: Boolean = false) : Event(async) {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}