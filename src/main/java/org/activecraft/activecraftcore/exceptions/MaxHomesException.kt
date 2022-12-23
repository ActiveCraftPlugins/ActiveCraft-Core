package org.activecraft.activecraftcore.exceptions

import org.activecraft.activecraftcore.playermanagement.Profile

class MaxHomesException(
    val profile: Profile,
    message: String? = "${profile.name} has reached their maximum amount of homes."
) : ActiveCraftException(message)