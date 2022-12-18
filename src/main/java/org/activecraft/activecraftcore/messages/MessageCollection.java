package org.activecraft.activecraftcore.messages;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.bukkit.ChatColor;

public abstract class MessageCollection {

    // msg
    public static String getMessage(String input) {
        return getMessage(input, ChatColor.GOLD);
    }

    public static String getMessage(String input, ChatColor color) {
        return getMessage(input, ActiveCraftCore.getInstance(), color);
    }

    public static String getMessage(String input, ActiveCraftPlugin plugin) {
        return getMessage(input, plugin, ChatColor.GOLD);
    }

    public static String getMessage(String input, ActiveCraftPlugin plugin, ChatColor color) {
        return ActiveCraftMessage.getMessage(input, plugin, color);
    }

    // raw msg
    public static String getRawMessage(String input) {
        return getRawMessage(input, ActiveCraftCore.getInstance());
    }

    public static String getRawMessage(String input, ActiveCraftPlugin plugin) {
        return getMessage(input, plugin, null);
    }

    // format
    public static String getFormatted(String key, MessageFormatter formatter) {
        return getFormatted(key, formatter, ChatColor.GOLD);
    }

    public static String getFormatted(String key, MessageFormatter formatter, ChatColor color) {
        return getFormatted(key, ActiveCraftCore.getInstance(), formatter, color);
    }

    public static String getFormatted(String key, ActiveCraftPlugin plugin, MessageFormatter formatter) {
        return getFormatted(key, plugin, formatter, ChatColor.GOLD);
    }

    public static String getFormatted(String key, ActiveCraftPlugin plugin, MessageFormatter formatter, ChatColor color) {
        return formatter.format(getMessage(key, plugin, color));
    }

}
