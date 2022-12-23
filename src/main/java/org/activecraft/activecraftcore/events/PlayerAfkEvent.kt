package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Profile

class PlayerAfkEvent(val profile: Profile, var afk: Boolean) : CancellableActiveCraftEvent()