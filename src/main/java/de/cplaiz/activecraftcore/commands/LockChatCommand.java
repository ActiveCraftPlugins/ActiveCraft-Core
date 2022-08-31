package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LockChatCommand extends ActiveCraftCommand {


    public LockChatCommand(ActiveCraftPlugin plugin) {
        super("lockchat", plugin);
    }

    @Override
    protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        ActiveCraftCore.getInstance().getMainConfig().set("lock-chat", Boolean.parseBoolean(args[0]));
    }

    @Override
    protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? List.of("true", "false") : null;
    }
}

