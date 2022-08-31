package de.cplaiz.activecraftcore.messages;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageFormatter {

    private CommandSender sender;
    private Profilev2 target;
    private HashMap<String, Placeholder> replacements = new HashMap<>();


    public MessageFormatter(CommandSender sender) {
        this.sender = sender;
    }

    public MessageFormatter(Profilev2 target) {
        this.target = target;
    }

    public MessageFormatter(String... replacements) {
        addReplacements(replacements);
    }

    public MessageFormatter(ChatColor color, String... replacements) {
        addReplacements(color, replacements);
    }

    public MessageFormatter(Placeholder... placeholder) {
        addReplacements(placeholder);
    }

    public MessageFormatter(Profilev2 target, String... replacements) {
        this.target = target;
        addReplacements(replacements);
    }

    public MessageFormatter(CommandSender sender, String... replacements) {
        this.sender = sender;
        addReplacements(replacements);
    }

    public MessageFormatter(Profilev2 target, CommandSender sender, String... replacements) {
        this.sender = sender;
        this.target = target;
        addReplacements(replacements);
    }


    public MessageFormatter setSender(CommandSender sender) {
        this.sender = sender;
        return this;
    }

    public MessageFormatter setSender(String sender) {
        addReplacements("s_playername", sender, "s_displayname", sender);
        return this;
    }

    public MessageFormatter setTarget(Profilev2 target) {
        this.target = target;
        return this;
    }

    public MessageFormatter setTarget(String target) {
        addReplacements("t_playername", target, "t_displayname", target);
        return this;
    }

    public MessageFormatter removePlacement(Placeholder placeholder) {
        return removePlacement(placeholder.getTarget());
    }

    public MessageFormatter removePlacement(String target) {
        replacements.remove(target);
        return this;
    }

    public MessageFormatter clearReplacements() {
        replacements.clear();
        return this;
    }

    public MessageFormatter addReplacement(Placeholder placeholder) {
        replacements.put(placeholder.getTarget(), placeholder);
        return this;
    }

    public MessageFormatter addReplacements(Placeholder... placeholder) {
        Arrays.stream(placeholder).forEach(this::addReplacement);
        return this;
    }

    public MessageFormatter addReplacements(String... replacements) {
        for (int i = 0; i < Math.floor((float) replacements.length / 2) * 2; i += 2)
            addReplacement(replacements[i], replacements[i + 1]);
        return this;
    }

    public MessageFormatter addReplacements(ChatColor color, String... replacements) {
        for (int i = 0; i < Math.floor((float) replacements.length / 2) * 2; i += 2)
            addReplacement(replacements[i], replacements[i + 1], color);
        return this;
    }

    public MessageFormatter addReplacement(String target, String replacement) {
        return addReplacement(target, replacement, ChatColor.AQUA);
    }

    public MessageFormatter addReplacement(String target, String replacement, ChatColor preColor) {
        return addReplacement(target, replacement, preColor, ChatColor.GOLD);
    }

    public MessageFormatter addReplacement(String target, String replacement, ChatColor preColor, ChatColor postColor) {
        return addReplacement(new Placeholder(target, replacement, preColor, postColor));
    }

    public String format(String message) {
        if (target != null) {
            addReplacements("t_playername", target.getName(), "t_displayname", target.getNickname());
        }
        if (sender != null) {
            Profilev2 profile = Profilev2.of(sender);
            if (profile != null) {
                addReplacements("s_playername", profile.getName(), "s_displayname", profile.getNickname());
            } else {
                addReplacements("s_playername", sender.getName(),
                        "s_displayname", sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName());
            }
        }
        for (Placeholder placeholder : replacements.values()) {
            message = message.replace("%" + placeholder.getTarget() + "%",
                            (placeholder.getPreColor() != null ? placeholder.getPreColor() : "")
                                    + placeholder.getReplacement()
                                    + (placeholder.getPostColor() != null ? placeholder.getPostColor() : ""));
        }
        return message;
    }
}
