package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Location

class PlayerWarpEvent(val profile: Profile, var location: Location, var warpName: String) : CancellableActiveCraftEvent()