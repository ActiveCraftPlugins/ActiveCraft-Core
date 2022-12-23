package org.activecraft.activecraftcore.events

import org.bukkit.Location

class WarpCreateEvent(var location: Location, var warpName: String) : CancellableActiveCraftEvent()