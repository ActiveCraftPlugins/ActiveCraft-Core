package de.silencio.activecraftcore.listener;

import de.silencio.activecraftcore.playermanagement.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GamemodeChangeListener implements Listener {

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.of(player);
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
