package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUnvanishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Profile profile;
    private boolean cancelled;

    public PlayerUnvanishEvent(Profile profile) {
        this.profile = profile;
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
