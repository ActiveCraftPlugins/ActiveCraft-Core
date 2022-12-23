package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
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
        sendMessage(sender, cmdMsg("locked-chat"));
    }

    @Override
    protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? List.of("true", "false") : null;
    }
}

