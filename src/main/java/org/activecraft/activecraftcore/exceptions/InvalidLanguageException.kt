package org.activecraft.activecraftcore.exceptions

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.messages.Language

class InvalidLanguageException @JvmOverloads constructor(
    val language: Language?,
    val plugin: ActiveCraftPlugin,
    message: String? = "Invalid language."
) : ActiveCraftException(message)