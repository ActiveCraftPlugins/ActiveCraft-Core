package de.cplaiz.activecraftcore.events;

import de.cplaiz.activecraftcore.playermanagement.Home;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Location;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerHomeDeleteEvent extends ActiveCraftEvent {
    private final Profilev2 profile;
    private final Home home;
    private boolean cancelled;

    public PlayerHomeDeleteEvent(Profilev2 profile, Home home) {
        this.profile = profile;
        this.home = home;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
