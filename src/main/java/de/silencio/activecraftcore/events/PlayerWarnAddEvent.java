package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Date;

public class PlayerWarnAddEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Profile target;
    private String reason;
    private Date date;
    private boolean cancelled;

    public PlayerWarnAddEvent(Profile target, String reason, Date date, String source) {
        this.target = target;
        this.reason = reason;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
