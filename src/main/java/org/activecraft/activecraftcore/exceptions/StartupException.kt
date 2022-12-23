package org.activecraft.activecraftcore.exceptions

class StartupException(val shutdown: Boolean, message: String?) : ActiveCraftException(message)