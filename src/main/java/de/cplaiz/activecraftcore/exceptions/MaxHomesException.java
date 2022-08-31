package de.cplaiz.activecraftcore.exceptions;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Getter;

@Getter
public class MaxHomesException extends ActiveCraftException {

    private final Profilev2 profile;

    public MaxHomesException(String message, Profilev2 profile) {
        super(message);
        this.profile = profile;
    }

    public MaxHomesException(Profilev2 profile) {
        this(profile.getName() + " has reached their maximum amount of homes.", profile);
    }

    public Profilev2 getProfile() {
        return profile;
    }
}
