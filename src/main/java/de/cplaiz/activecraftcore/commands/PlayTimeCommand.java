package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PlayTimeCommand extends ActiveCraftCommand {

    public PlayTimeCommand(ActiveCraftPlugin plugin) {
        super("playtime",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        if (type == CommandTargetType.SELF) checkIsPlayer(sender);
        Profilev2 profile = type == CommandTargetType.SELF ? getProfile(sender) : getProfile(args[0]);
        checkPermission(sender, type.code());
        isTargetSelf(sender, profile.getName());
        messageFormatter.setTarget(profile);
        int playtime = profile.getPlaytime();
        int playtimeMinutes = playtime * 60;
        int playtimeHours = (playtime - playtimeMinutes) / 60;
        messageFormatter.addReplacements("hours", playtimeHours + "", "minutes", playtimeHours + "");
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getProfileNames() : null;
    }
}