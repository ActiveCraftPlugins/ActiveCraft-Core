package org.activecraft.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class LockdownEvent extends ActiveCraftEvent {

    private @NonNull boolean lockedDown;
    private boolean cancelled;
}
