package org.activecraft.activecraftcore.events

import org.bukkit.Location

class WarpDeleteEvent(var location: Location, var warpName: String) : CancellableActiveCraftEvent()