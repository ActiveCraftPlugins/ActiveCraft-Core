package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerMuteEvent extends ActiveCraftEvent {

    private final Profile target;
    private @NonNull boolean muted;
    private boolean cancelled;

}
