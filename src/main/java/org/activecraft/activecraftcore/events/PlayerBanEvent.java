package org.activecraft.activecraftcore.events;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PlayerBanEvent extends ActiveCraftEvent {

    private final String target;
    private @NonNull String reason;
    private Date expirationDate;
    private @NonNull String source;
    private boolean cancelled;

    public PlayerBanEvent(String target, @NotNull String reason, Date expirationDate, @NotNull String source) {
        this.target = target;
        this.reason = reason;
        this.expirationDate = expirationDate;
        this.source = source;
    }

}
