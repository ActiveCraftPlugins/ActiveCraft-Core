package de.silencio.activecraftcore.manager;

import de.silencio.activecraftcore.events.PlayerHomeDeleteEvent;
import de.silencio.activecraftcore.events.PlayerHomeSetEvent;
import de.silencio.activecraftcore.events.PlayerHomeTeleportEvent;
import de.silencio.activecraftcore.exceptions.*;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.playermanagement.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.HashMap;

// TODO: 03.02.2022 events 

public class HomeManager {

    private Profile profile;

    public HomeManager(Profile profile) {
        this.profile = profile;
    }

    public void create(String name, Location location, boolean ignoreMaxHomes) throws ActiveCraftException {
        Player target;
        HashMap<String, Location> homeList = profile.getHomeList();
        int maxHomes = 1;
        if (ignoreMaxHomes && (target = profile.getPlayer()) != null) {
            for (PermissionAttachmentInfo perm : target.getEffectivePermissions())
                if (perm.getPermission().startsWith("activecraft.maxhomes."))
                    if (Bukkit.getPluginManager().getPermission(perm.getPermission()) == null)
                        Bukkit.getPluginManager().addPermission(new Permission(perm.getPermission()));

            for (Permission perm : Bukkit.getPluginManager().getPermissions())
                if (perm.getName().startsWith("activecraft.maxhomes."))
                    maxHomes = Integer.parseInt(perm.getName().split("\\.")[2]);

            if (!(homeList.keySet().size() < maxHomes || target.isOp())) {
                target.sendMessage(Errors.WARNING() + CommandMessages.MAX_HOMES());
                throw new MaxHomesException(profile);
            }
        }

        // call event
        PlayerHomeSetEvent event = new PlayerHomeSetEvent(profile, homeList.get(name));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        homeList.put(name, location);
        profile.set(Profile.Value.HOME_LIST, homeList);
    }

    public void create(String name, Location location) throws ActiveCraftException {
        create(name, location, false);
    }

    public void remove(String name) throws ActiveCraftException {
        HashMap<String, Location> homeList = profile.getHomeList();
        if (!homeList.containsKey(name)) throw new InvalidHomeException(name, profile);

        // call event
        PlayerHomeDeleteEvent event = new PlayerHomeDeleteEvent(profile, homeList.get(name));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        homeList.remove(name);
        profile.set(Profile.Value.HOME_LIST, homeList);
    }

    public void teleportHome(String name) throws ActiveCraftException {
        Player player = profile.getPlayer();
        HashMap<String, Location> homeList = profile.getHomeList();
        if (!homeList.containsKey(name)) throw new InvalidHomeException(name, profile);

        // call event
        PlayerHomeTeleportEvent event = new PlayerHomeTeleportEvent(profile, homeList.get(name));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        player.teleport(homeList.get(name));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
    }
}
