package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.Warn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerWarnAddEvent extends ActiveCraftEvent {

    private final Profile target;
    private @NonNull Warn warn;
    private boolean cancelled;

}
