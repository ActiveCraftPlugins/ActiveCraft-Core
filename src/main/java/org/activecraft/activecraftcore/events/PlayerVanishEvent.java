package org.activecraft.activecraftcore.events;

import org.activecraft.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerVanishEvent extends ActiveCraftEvent {

    private final Profilev2 profile;
    private @NonNull boolean vanished;
    private boolean cancelled;

}
