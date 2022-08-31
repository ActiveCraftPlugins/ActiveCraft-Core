package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ToggleSocialSpyCommand extends ActiveCraftCommand {

    // TODO: 11.06.2022 testen mit 2 person

    public ToggleSocialSpyCommand(ActiveCraftPlugin plugin) {
        super("togglesocialspy",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        Player player = getPlayer(sender);
        Profilev2 profile = getProfile(player);
        profile.setReceiveSocialspy(!profile.canReceiveSocialspy());
        sendMessage(sender, this.cmdMsg((profile.canReceiveSocialspy() ? "en" : "dis") + "abled"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return  null;
    }

}