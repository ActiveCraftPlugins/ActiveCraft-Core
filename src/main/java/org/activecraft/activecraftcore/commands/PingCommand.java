package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PingCommand extends ActiveCraftCommand {

    public PingCommand(ActiveCraftPlugin plugin) {
        super("ping",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        if (!(sender instanceof Player)) {
            sendMessage(sender, this.cmdMsg("console"));
            return;
        }
        Player player = getPlayer(sender);
        checkPermission(sender);
        messageFormatter.addReplacement("ping", player.getPing() + "");
        sendMessage(sender, this.cmdMsg("player"));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}