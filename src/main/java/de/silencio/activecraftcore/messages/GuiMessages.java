package de.silencio.activecraftcore.messages;

import org.bukkit.ChatColor;

public class GuiMessages extends ActiveCraftCoreMessage {

    public static String BACK_ARROW() {
        return ChatColor.GOLD + getMessage(MessageType.GUI, "back-arrow");
    }

    public static String CLOSE_ITEM() {
        return ChatColor.GOLD + getMessage(MessageType.GUI, "close-item");
    }

    public static String CONFIRM_ITEM() {
        return ChatColor.GOLD + getMessage(MessageType.GUI, "confirm-item");
    }

    public static String CANCEL_ITEM() {
        return ChatColor.GOLD + getMessage(MessageType.GUI, "cancel-item");
    }

    public static String CONFIRMATION_TITLE() {
        return ChatColor.GOLD + getMessage(MessageType.GUI, "confirmation-title");
    }

    public static String DEFAULT_GUI_TITLE() {
        return ChatColor.GOLD + getMessage(MessageType.GUI, "default-gui-title");
    }

    public static String NO_PERMISSION_ITEM() {
        return ChatColor.GOLD + getMessage(MessageType.GUI, "no-permission-item");
    }
}