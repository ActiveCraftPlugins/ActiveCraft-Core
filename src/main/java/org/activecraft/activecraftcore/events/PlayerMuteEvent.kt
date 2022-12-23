package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Profile

class PlayerMuteEvent(val target: Profile, var muted: Boolean) : CancellableActiveCraftEvent()