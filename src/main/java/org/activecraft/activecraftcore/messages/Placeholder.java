package org.activecraft.activecraftcore.messages;

import org.activecraft.activecraftcore.messagesv2.ColorScheme;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Placeholder {

    private final String target;
    private String replacement;
    private ChatColor preColor;
    private ChatColor postColor;

    public Placeholder(String target, String replacement) {
        this(target, replacement, ChatColor.AQUA);
    }

    public Placeholder(String target, String replacement, ChatColor color) {
        this(target, replacement, color, ChatColor.GOLD);
    }

    public Placeholder(String target, String replacement, ChatColor preColor, ChatColor postColor) {
        this.postColor = postColor;
        this.preColor = preColor;
        this.replacement = replacement;
        this.target = target;
    }

}
