package de.cplaiz.activecraftcore.exceptions;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Getter;

@Getter
public class PlayerOfflineException extends ActiveCraftException {

    private final Profilev2 profile;

    public PlayerOfflineException(String message, Profilev2 profile) {
        super(message);
        this.profile = profile;
    }

    public PlayerOfflineException(Profilev2 profile) {
        this(profile.name + " is currently offline!", profile);
    }

}
