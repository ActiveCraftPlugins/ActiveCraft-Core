package de.cplaiz.activecraftcore.messages;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import org.bukkit.ChatColor;

public class Errors extends MessageCollection implements ActiveCraftCore.MessageImpl {

    private static final String PREFIX = "error.";

    public static String getWarningMessage(String key) {
        return WARNING() + getMessage(PREFIX + key, ChatColor.GRAY);
    }

    public static String WARNING() {
        return getMessage(PREFIX + "general-warning", ChatColor.RED) + " ";
    }

    public static String NO_PERMISSION() {
        return getWarningMessage("no-permission");
    }

    public static String INVALID_PLAYER() {
        return getWarningMessage("invalid-player");
    }

    public static String INVALID_NUMBER() {
        return getWarningMessage("invalid-number");
    }

    public static String INVALID_ARGUMENTS() {
        return getWarningMessage("invalid-arguments");
    }

    public static String TOO_MANY_ARGUMENTS() {
        return getWarningMessage("too-many-arguments");
    }

    public static String CANNOT_TARGET_SELF() {
        return getWarningMessage("cannot-target-self");
    }

    public static String INVALID_COLOR() {
        return getWarningMessage("invalid-color");
    }

    public static String INVALID_HEX() {
        return getWarningMessage("invalid-hex");
    }

    public static String INVALID_ENTITY() {
        return getWarningMessage("invalid-entity");
    }

    public static String NUMBER_TOO_LARGE() {
        return getWarningMessage("number-too-large");
    }

    public static String NOT_A_PLAYER() {
        return getWarningMessage("not-a-player");
    }

    public static String NOT_TRUE_FALSE() {
        return getWarningMessage("not-true-false");
    }

    public static String NOT_HOLDING_ITEM() {
        return getWarningMessage("not-holding-item");
    }

    public static String INVALID_WORLD() {
        return getWarningMessage("invalid-world");
    }

    public static String INVALID_COMMAND() {
        return getWarningMessage("invalid-command");
    }

    public static String OPERATION_FAILED() {
        return getWarningMessage("operation-failed");
    }

    public static String INVALID_LANGUAGE() {
        return getWarningMessage("invalid-language");
    }
}
