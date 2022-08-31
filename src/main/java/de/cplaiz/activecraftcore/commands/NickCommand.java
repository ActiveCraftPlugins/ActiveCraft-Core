package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.manager.NickManager;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.ColorUtils;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class NickCommand extends ActiveCraftCommand {

    public NickCommand(ActiveCraftPlugin plugin) {
        super("nick", plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkArgsLength(args, ComparisonType.GREATER, 0);
        CommandTargetType type = (args.length == 1 || Profilev2.of(args[0]) == null) ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        if (type == CommandTargetType.SELF) checkIsPlayer(sender);
        Profilev2 profile = type == CommandTargetType.SELF ? getProfile(sender) : getProfile(args[0]);
        messageFormatter.setTarget(profile);
        args = trimArray(args, type == CommandTargetType.SELF ? 0 : 1);
        checkPermission(sender, type.code());
        String nickname = concatArray(args);
        nickname = ColorUtils.replaceColorAndFormat(nickname);
        messageFormatter.addReplacement("nickname", nickname);
        if (type == CommandTargetType.OTHERS)
            if (!isTargetSelf(sender, profile.getName()) && profile.getPlayer() != null)
                sendSilentMessage(profile.getPlayer(), cmdMsg("target-message"));
        sendMessage(sender, cmdMsg(type.code()));
        NickManager.nick(profile, nickname);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getProfileNames() : null;
    }
}