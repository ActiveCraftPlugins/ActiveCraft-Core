package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWarpEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Profile profile;
    private Location location;
    private String warpName;
    private boolean cancelled;

    public PlayerWarpEvent(Profile profile, Location location, String warpName) {
        this.profile = profile;
        this.location = location;
        this.warpName = warpName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getWarpName() {
        return warpName;
    }

    public void setWarpName(String warpName) {
        this.warpName = warpName;
    }

    public Profile getProfile() {
        return profile;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = true;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
