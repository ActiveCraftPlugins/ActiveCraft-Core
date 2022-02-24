package de.silencio.activecraftcore.utils.config;

import de.silencio.activecraftcore.playermanagement.Profile;

public record Warn(Profile holder, String reason, String created, String source, String id) {
}
