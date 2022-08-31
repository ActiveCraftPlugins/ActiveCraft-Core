package de.cplaiz.activecraftcore.messages;

import org.bukkit.ChatColor;

public record MessageSupplier(ActiveCraftMessage activeCraftMessage, Language language) {

    public String getMessage(String input) {
        return activeCraftMessage.getMessage(input);
    }

    public String getMessage(String input, ChatColor color) {
        return activeCraftMessage.getMessage(input, color, language);
    }

    // raw msg
    public String getRawMessage(String input) {
        return activeCraftMessage.getRawMessage(input);
    }

    // format
    public String getFormatted(String key, MessageFormatter formatter) {
        return activeCraftMessage.getFormatted(key, formatter);
    }


    public String getFormatted(String key, MessageFormatter formatter, ChatColor color) {
        return activeCraftMessage.getFormatted(key, formatter, color);
    }

}
