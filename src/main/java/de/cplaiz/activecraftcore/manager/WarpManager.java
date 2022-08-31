package de.cplaiz.activecraftcore.manager;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.events.PlayerWarpEvent;
import de.cplaiz.activecraftcore.events.WarpCreateEvent;
import de.cplaiz.activecraftcore.events.WarpDeleteEvent;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;
import java.util.Map;

public class WarpManager {

    public static Location getWarp(String name) {
        return ActiveCraftCore.getInstance().getWarpsConfig().getWarps().get(name);
    }

    public static void createWarp(String name, Location location) {
        //call event
        WarpCreateEvent event = new WarpCreateEvent(location, name);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        //add permissions
        Map<String, Boolean> childMap = new HashMap<>();
        childMap.put("activecraft.warp.self", true);
        childMap.put("activecraft.warp", true);
        Bukkit.getPluginManager().addPermission(new Permission("activecraft.warp.self." + name,
                "Permission to warp yourself to a specific warp.", PermissionDefault.OP, childMap));
        childMap.clear();
        childMap.put("activecraft.warp.others", true);
        childMap.put("activecraft.warp", true);
        Bukkit.getPluginManager().addPermission(new Permission("activecraft.warp.others." + name,
                "Permission to warp another player to a specific warp.", PermissionDefault.OP, childMap));

        //add warp to config
        ActiveCraftCore.getInstance().getWarpsConfig().set(event.getWarpName(), event.getLocation(), true);
    }

    public static void deleteWarp(String name) {
        //call event
        WarpDeleteEvent event = new WarpDeleteEvent(getWarp(name), name);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        //remove permissions
        Bukkit.getPluginManager().removePermission("activecraft.warp.self." + name);
        Bukkit.getPluginManager().removePermission("activecraft.warp.others." + name);

        //add warp to config
        ActiveCraftCore.getInstance().getWarpsConfig().set(event.getWarpName(), null, true);
    }

    public static void warp(Player player, String warpName) {
        //call event
        PlayerWarpEvent event = new PlayerWarpEvent(Profilev2.of(player), getWarp(warpName), warpName);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        //teleport
        player.teleport(event.getLocation());
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
    }
}
