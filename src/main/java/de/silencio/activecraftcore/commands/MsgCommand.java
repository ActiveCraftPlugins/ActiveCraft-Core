package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.manager.MessageManager;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.ColorUtils;
import de.silencio.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MsgCommand extends ActiveCraftCommand {

    public MsgCommand() {
        super("msg");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "msg");
        checkArgsLength(args, ComparisonType.GREATER , 1);
        Profile profile = getProfile(args[0]);
        checkTargetSelf(sender, profile.getName());

        String message = combineArray(args, 1);
        message = ColorUtils.replaceColorAndFormat(message);
        sendMessage(sender, CommandMessages.MSG_PREFIX_TO(profile, message));
        MessageManager.sendDM(profile, sender, message);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getProfileNames() : null;
    }
}