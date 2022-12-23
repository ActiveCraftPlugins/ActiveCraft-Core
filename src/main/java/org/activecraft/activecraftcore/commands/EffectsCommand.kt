package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.effectgui.EffectGui;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.effectgui.EffectGui;
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
