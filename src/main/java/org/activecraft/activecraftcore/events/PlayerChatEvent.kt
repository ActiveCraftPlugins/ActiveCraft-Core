package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Profile

class PlayerChatEvent(val profile: Profile, var message: String, isAsync: Boolean) :
    CancellableActiveCraftEvent(isAsync)