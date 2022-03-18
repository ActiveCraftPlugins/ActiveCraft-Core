package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.ColorUtils;
import de.silencio.activecraftcore.utils.ComparisonType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RealNameCommand extends ActiveCraftCommand {

    public RealNameCommand() {
        super("realname");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        List<String> associatedPlayerList = new ArrayList<>();
        checkPermission(sender, "realname");
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        String displayname = combineArray(args, 0).trim();
        sendMessage(sender, CommandMessages.REALNAME_HEADER(combineList(ActiveCraftCore.getProfiles().values().stream()
                        .filter(profile -> displayname.equalsIgnoreCase(ColorUtils.removeColorAndFormat(profile.getNickname())))
                        .map(Profile::getName)
                .collect(Collectors.toList()), 0, ", "), displayname));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(player -> getProfile(player).getRawNickname()).collect(Collectors.toList()) : null;
    }
}