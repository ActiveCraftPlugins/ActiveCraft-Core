package de.cplaiz.activecraftcore.playermanagement

import java.time.LocalDateTime

data class Warn(val id: String, val reason: String, val created: LocalDateTime, val source: String)
