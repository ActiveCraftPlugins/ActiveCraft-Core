package org.activecraft.activecraftcore.exceptions

import org.activecraft.activecraftcore.playermanagement.Profile

class PlayerOfflineException(val profile: Profile, message: String? = "${profile.name} is currently offline!") :
    ActiveCraftException(message)