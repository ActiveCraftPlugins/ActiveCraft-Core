package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerAfkEvent extends ActiveCraftEvent {

    private final Profile profile;
    private @NonNull boolean afk;
    private boolean cancelled;
}
