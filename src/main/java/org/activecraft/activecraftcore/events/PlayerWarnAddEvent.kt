package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.playermanagement.Warn

class PlayerWarnAddEvent(val target: Profile, var warn: Warn) : CancellableActiveCraftEvent()