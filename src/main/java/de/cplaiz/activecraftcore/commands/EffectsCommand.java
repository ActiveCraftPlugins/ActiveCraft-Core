package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.guicreator.GuiNavigator;
import de.cplaiz.activecraftcore.guis.effectgui.EffectGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class EffectsCommand extends ActiveCraftCommand {

    public EffectsCommand(ActiveCraftPlugin plugin) {
        super("effects",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Player player = getPlayer(sender);
        Player target = args.length == 0 ? player : getPlayer(args[0]);
        EffectGui effectGui = new EffectGui(player, target);
        GuiNavigator.push(player, effectGui.getPotionEffectGui().build());
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
