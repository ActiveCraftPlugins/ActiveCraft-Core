package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.List;

public class ButcherCommand extends ActiveCraftCommand {

    public ButcherCommand(ActiveCraftPlugin plugin) {
        super("butcher",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Player player = getPlayer(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 0);
        List<Entity> entities = player.getNearbyEntities(200, 500, 200).stream()
                .filter(e -> e instanceof Monster || e instanceof Flying || e instanceof Slime)
                .toList();
        if(entities.size() == 0) {
            sendMessage(sender, this.rawCmdMsg("no-mobs"), true);
            return;
        }

        int killed = 0;
        for (Entity e : entities) {
            ((Damageable) e).setHealth(0);
            killed += 1;
        }
        messageFormatter.addReplacement("amount", killed + "");
        sendMessage(sender, this.cmdMsg("killed-mobs"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}