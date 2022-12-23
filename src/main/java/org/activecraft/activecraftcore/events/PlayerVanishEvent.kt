package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Profile

class PlayerVanishEvent(val profile: Profile, var vanished: Boolean) : CancellableActiveCraftEvent()