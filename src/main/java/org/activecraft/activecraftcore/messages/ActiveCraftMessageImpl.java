package org.activecraft.activecraftcore.messages;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.bukkit.ChatColor;

public interface ActiveCraftMessageImpl {

    // msg
    String msg(String input);

    String msg(String input, ChatColor color);

    default String msg(String input, ActiveCraftPlugin plugin) {
        return msg(input, plugin, ChatColor.GOLD);
    }

    default String msg(String input, ActiveCraftPlugin plugin, ChatColor color) {
        return ActiveCraftMessage.getMessage(input, plugin, color);
    }

    // format
    String formatMsg(String key, MessageFormatter formatter);

    String formatMsg(String key, MessageFormatter formatter, ChatColor color);

    default String formatMsg(String key, ActiveCraftPlugin plugin, MessageFormatter formatter) {
        return formatMsg(key, plugin, formatter, ChatColor.GOLD);
    }

    default String formatMsg(String key, ActiveCraftPlugin plugin, MessageFormatter formatter, ChatColor color) {
        return ActiveCraftMessage.getFormatted(key, plugin, formatter, color);
    }

    // rawmsg
    String rawMsg(String input);

    default String rawMsg(String input, ActiveCraftPlugin plugin) {
        return msg(input, plugin, null);
    }
}
