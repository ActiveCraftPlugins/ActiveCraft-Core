package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.manager.VanishManager;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VanishCommand extends ActiveCraftCommand {

    // TODO: 12.06.2022 nochmal testen mit 2 Spielern

    public VanishCommand(ActiveCraftPlugin plugin) {
        super("vanish",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length >= 1 ? CommandTargetType.OTHERS : CommandTargetType.SELF;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        Profilev2 profile = getProfile(target);
        checkPermission(sender, type.code());
        VanishManager.setVanished(profile, !profile.isVanished(), sender, false);
        messageFormatter.setTarget(profile)
                .addReplacements("displayname", profile.getNickname(), "playername", profile.getName());
        sendMessage(sender, this.cmdMsg("now-" + (profile.isVanished() ? "in" : "") + "visible-" + type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}