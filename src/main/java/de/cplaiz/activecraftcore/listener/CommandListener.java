package de.cplaiz.activecraftcore.listener;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.messagesv2.ActiveCraftMessage;
import de.cplaiz.activecraftcore.messagesv2.PlayerMessageFormatter;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandSent(PlayerCommandPreprocessEvent event) {
        Player executingPlayer = event.getPlayer();
        Profilev2 executingPlayerProfile = Profilev2.of(executingPlayer);
        String eventMessage = event.getMessage();
        ActiveCraftMessage accoreMessage = ActiveCraftCore.getInstance().getActiveCraftMessagev2();

        for (Player player : Bukkit.getOnlinePlayers()) {
            Profilev2 profile = Profilev2.of(player);
            if (!profile.canReceiveLog()) continue;
            if (!player.hasPermission("activecraft.log")) continue;
            player.sendMessage(
                    executingPlayerProfile.getMessageSupplier(accoreMessage).getFormatted(
                            "command.log.format",
                            new PlayerMessageFormatter(accoreMessage, executingPlayerProfile).addFormatterPattern("command", eventMessage)
                    ));
        }
    }

}
