package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.manager.MessageManager;
import de.cplaiz.activecraftcore.utils.ColorUtils;
import de.cplaiz.activecraftcore.utils.ComparisonType;
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