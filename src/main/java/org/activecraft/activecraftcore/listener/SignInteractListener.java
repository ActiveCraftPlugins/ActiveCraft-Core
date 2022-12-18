package org.activecraft.activecraftcore.listener;

import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignInteractListener implements Listener {

    public void log(String text) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + text);
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        BlockState blockState = event.getClickedBlock().getState();
        if (blockState instanceof Sign) {
            if (player.isSneaking()) {
                Profilev2 profile = Profilev2.of(player);
                if (profile.canEditSign()) {
                    event.setCancelled(true);
                    Sign signBlock = (Sign) event.getClickedBlock().getState();
                    player.openSign(signBlock);
                }
            }
        }
    }

}