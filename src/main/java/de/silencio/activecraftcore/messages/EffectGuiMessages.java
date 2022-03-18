package de.silencio.activecraftcore.messages;

import de.silencio.activecraftcore.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;

public class EffectGuiMessages extends ActiveCraftCoreMessage {

    public static String HEADER_STATUSEFFECTS() {
        return ChatColor.GOLD + getMessage(MessageType.EFFECTGUI, "header.statuseffects");
    }

    public static String HEADER_POTIONEFFECTS() {
        return ChatColor.GOLD + getMessage(MessageType.EFFECTGUI, "header.potioneffects");
    }

    public static String ITEM_ACTIVE_FORMAT() {
        return ChatColor.GREEN + getMessage(MessageType.EFFECTGUI, "effectitem.active-format");
    }

    public static String ITEM_INACTIVE_FORMAT() {
        return ChatColor.RED + getMessage(MessageType.EFFECTGUI, "effectitem.inactive-format");
    }

    public static String ITEM_LEVEL_FORMAT(int level) {
        return ChatColor.GOLD + getMessage(MessageType.EFFECTGUI, "effectitem.level-format")
                .replace("%level%", ChatColor.AQUA + String.valueOf(level) + ChatColor.GOLD);
    }

    public static String ITEM_TOOLTIP() {
        return ChatColor.GRAY + getMessage(MessageType.EFFECTGUI, "effectitem.tooltip");
    }

    public static String EFFECT(PotionEffectType potionEffectType) {
        return ColorUtils.getRgbColorCode(potionEffectType.getColor())
                + getMessage(MessageType.EFFECTGUI, "effects." + potionEffectType.getName().toLowerCase().replace("_", "-"));
    }

    public static String TITLE() {
        return ChatColor.GOLD + getMessage(MessageType.EFFECTGUI, "title");
    }
}
