package de.silencio.activecraftcore.exceptions;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Getter;

@Getter
public class MaxHomesException extends ActiveCraftException {

    private final Profile profile;

    public MaxHomesException(String message, Profile profile) {
        super(message);
        this.profile = profile;
    }

    public MaxHomesException(Profile profile) {
        this(profile.getName() + " has reached their maximum amount of homes.", profile);
    }
}
