package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.manager.MessageManager;
import de.silencio.activecraftcore.utils.ColorUtils;
import de.silencio.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReplyCommand extends ActiveCraftCommand {

    public ReplyCommand() {
        super("reply");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        Player player = getPlayer(sender);
        checkPermission(sender, "reply");
        checkArgsLength(args, ComparisonType.GREATER, 0);

        MessageManager.reply(sender, ColorUtils.replaceColorAndFormat(combineArray(args)));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}