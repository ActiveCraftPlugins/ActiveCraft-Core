package de.silencio.activecraftcore.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class ActiveCraftEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}