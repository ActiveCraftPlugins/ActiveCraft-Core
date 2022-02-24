package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.Warn;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.time.OffsetDateTime;
import java.util.Date;

public class PlayerWarnAddEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Warn warn;
    private Profile target;
    private boolean cancelled;

    public PlayerWarnAddEvent(Profile target, Warn warn) {
        this.target = target;
        this.warn = warn;
    }

    public Profile getTarget() {
        return target;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = true;
    }

    public Warn getWarn() {
        return warn;
    }

    public void setWarn(Warn warn) {
        this.warn = warn;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
