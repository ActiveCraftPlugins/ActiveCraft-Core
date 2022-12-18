package org.activecraft.activecraftcore.events;

import org.activecraft.activecraftcore.playermanagement.Home;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Location;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerHomeTeleportEvent extends ActiveCraftEvent {
    private final Profilev2 profile;
    private final Home home;
    private boolean cancelled;

    public PlayerHomeTeleportEvent(Profilev2 profile, Home home) {
        this.profile = profile;
        this.home = home;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
