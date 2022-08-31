package de.cplaiz.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerUnbanEvent extends ActiveCraftEvent {

    private final String target;
    private boolean cancelled;

}
