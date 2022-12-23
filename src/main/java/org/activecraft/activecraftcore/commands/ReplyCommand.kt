package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.manager.MessageManager;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReplyCommand extends ActiveCraftCommand {

    public ReplyCommand(ActiveCraftPlugin plugin) {
        super("reply",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        Player player = getPlayer(sender);
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER, 0);
        String message = concatArray(args, 0);
        message = ColorUtils.replaceColorAndFormat(message);
        messageFormatter.addReplacement("message", message);
        messageFormatter.setTarget(MessageManager.getMsgPlayerStoring().get(player));
        MessageManager.msgToActiveConversation(sender, ColorUtils.replaceColorAndFormat(message));
        sendMessage(sender, getActiveCraftMessage().getMessage("command.msg.format-to"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}