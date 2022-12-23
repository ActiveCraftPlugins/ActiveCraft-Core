package org.activecraft.activecraftcore.exceptions

import org.activecraft.activecraftcore.playermanagement.Profile

class InvalidHomeException(
    @JvmField val invalidString: String,
    @JvmField val profile: Profile,
    message: String? = "$invalidString is not a home."
) : ActiveCraftException(message)