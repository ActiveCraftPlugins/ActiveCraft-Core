package de.cplaiz.activecraftcore.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class ActiveCraftEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private @Getter final boolean async;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ActiveCraftEvent() {
        this(false);
    }

    public ActiveCraftEvent(boolean isAsync) {
        super(isAsync);
        this.async = isAsync;
    }
}
