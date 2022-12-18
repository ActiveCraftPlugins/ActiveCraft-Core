package org.activecraft.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerUnbanEvent extends ActiveCraftEvent {

    private final String target;
    private boolean cancelled;

}
