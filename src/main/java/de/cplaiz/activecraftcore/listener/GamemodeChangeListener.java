package de.cplaiz.activecraftcore.listener;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GamemodeChangeListener implements Listener {

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        Profilev2 profile = Profilev2.of(player);
        if (!profile.isFly()) return;
        boolean wasFlying = player.isFlying();
        Thread thread = new Thread(() -> {
            while (true)
                if (player.getGameMode() == event.getNewGameMode()) break;
            player.setAllowFlight(true);
            player.setFlying(wasFlying);
        });
        thread.start();
    }
}
