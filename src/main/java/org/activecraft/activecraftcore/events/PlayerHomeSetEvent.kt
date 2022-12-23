package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Home
import org.activecraft.activecraftcore.playermanagement.Profile

class PlayerHomeSetEvent(val profile: Profile, var home: Home) : CancellableActiveCraftEvent()