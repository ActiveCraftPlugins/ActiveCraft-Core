package de.cplaiz.activecraftcore.messages;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import org.bukkit.ChatColor;

public record ColorScheme(ChatColor primary, ChatColor primaryAccent, ChatColor secondary, ChatColor secondaryAccent, ChatColor warningPrefix, ChatColor warningMessage) {
    public static final ColorScheme DEFAULT = new ColorScheme(ChatColor.GOLD, ChatColor.AQUA, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.RED, ChatColor.GRAY);

    public static ColorScheme of(ActiveCraftPlugin plugin) {
        return plugin.getActiveCraftMessage().getColorScheme();
    }
}