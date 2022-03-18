package de.silencio.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerIpBanEvent extends ActiveCraftEvent {

    private final String target;
    private boolean banned;
    private String reason;
    private Date expirationDate;
    private String source;
    private boolean cancelled;

    public PlayerIpBanEvent(String target, boolean banned, String reason, Date expirationDate, String source) {
        this.target = target;
        this.banned = banned;
        this.reason = reason;
        this.expirationDate = expirationDate;
        this.source = source;
    }

}
