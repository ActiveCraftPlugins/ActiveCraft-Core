package de.silencio.activecraftcore.messages;

import org.bukkit.ChatColor;

public class TableMenuMessages extends ActiveCraftCoreMessage {

    public static String TABLEMENU_TITLE() {
        return ChatColor.GOLD + getMessage(MessageType.TABLEMENU, "title");
    }

    public static String TABLEMENU_CRAFTING_TABLE() {
        return ChatColor.GOLD + getMessage(MessageType.TABLEMENU, "crafting-table");
    }

    public static String TABLEMENU_CARTOGRAPHY_TABLE() {
        return ChatColor.GOLD + getMessage(MessageType.TABLEMENU, "cartography-table");
    }

    public static String TABLEMENU_STONECUTTER() {
        return ChatColor.GOLD + getMessage(MessageType.TABLEMENU, "stonecutter");
    }

    public static String TABLEMENU_ANVIL() {
        return ChatColor.GOLD + getMessage(MessageType.TABLEMENU, "anvil");
    }

    public static String TABLEMENU_GRINDSTONE() {
        return ChatColor.GOLD + getMessage(MessageType.TABLEMENU, "grindstone");
    }

    public static String TABLEMENU_LOOM() {
        return ChatColor.GOLD + getMessage(MessageType.TABLEMENU, "loom");
    }

    public static String TABLEMENU_SMITHING_TABLE() {
        return ChatColor.GOLD + getMessage(MessageType.TABLEMENU, "smithing-table");
    }
}
