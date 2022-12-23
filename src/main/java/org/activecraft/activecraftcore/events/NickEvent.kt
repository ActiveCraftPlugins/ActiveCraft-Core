package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Profile

class NickEvent(val profile: Profile, var newName: String) : CancellableActiveCraftEvent()