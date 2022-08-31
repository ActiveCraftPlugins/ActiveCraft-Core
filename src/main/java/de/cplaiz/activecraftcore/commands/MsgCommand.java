package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.manager.MessageManager;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.ColorUtils;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MsgCommand extends ActiveCraftCommand {

    public MsgCommand(ActiveCraftPlugin plugin) {
        super("msg",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER , 1);
        Profilev2 profile = getProfile(args[0]);
        messageFormatter.setTarget(profile);
        isTargetSelf(sender, profile.getName(), true);

        String message = concatArray(args, 1);
        message = ColorUtils.replaceColorAndFormat(message);
        messageFormatter.addReplacement("message", message);
        sendMessage(sender, this.cmdMsg("format-to"));
        MessageManager.openConversation(sender, profile);
        MessageManager.msgToActiveConversation(sender, message);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getProfileNames() : null;
    }
}