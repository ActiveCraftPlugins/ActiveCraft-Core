package de.cplaiz.activecraftcore.events;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.playermanagement.Warn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerWarnAddEvent extends ActiveCraftEvent {

    private final Profilev2 target;
    private @NonNull Warn warn;
    private boolean cancelled;

    public PlayerWarnAddEvent(Profilev2 target, @NotNull Warn warn) {
        this.target = target;
        this.warn = warn;
    }

    @NotNull
    public Warn getWarn() {
        return warn;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
