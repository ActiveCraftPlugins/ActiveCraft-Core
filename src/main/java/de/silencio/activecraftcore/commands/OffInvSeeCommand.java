package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.offinvsee.OffInvSeeGui;
import de.silencio.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class OffInvSeeCommand extends ActiveCraftCommand {

    public OffInvSeeCommand() {
        super("offinvsee");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "offinvsee");
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        Player player = getPlayer(sender);
        Player target = getPlayer(args[0]);
        GuiNavigator.push(player, new OffInvSeeGui(player, target).build());
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return getBukkitPlayernames();
    }
}
