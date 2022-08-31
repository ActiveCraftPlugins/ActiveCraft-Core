package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.guicreator.GuiNavigator;
import de.cplaiz.activecraftcore.guis.offinvsee.OffInvSeeGui;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class OffInvSeeCommand extends ActiveCraftCommand {

    public OffInvSeeCommand(ActiveCraftPlugin plugin) {
        super("offinvsee",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        Player player = getPlayer(sender);
        Player target = getPlayer(args[0]);
        GuiNavigator.push(player, new OffInvSeeGui(player, target).build());
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getProfileNames() : null;
    }
}
