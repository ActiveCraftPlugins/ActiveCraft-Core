package org.activecraft.activecraftcore.modules;

import java.net.URL;

public record Module(String name, int id, String description, URL spigotPageUrl, URL downloadUrl) {
}

