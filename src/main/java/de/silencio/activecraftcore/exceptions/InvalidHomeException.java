package de.silencio.activecraftcore.exceptions;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Getter;

@Getter
public class InvalidHomeException extends ActiveCraftException {

    private final String invalidString;
    private final Profile profile;

    public InvalidHomeException(String message, String invalidString, Profile profile) {
        super(message);
        this.invalidString = invalidString;
        this.profile = profile;
    }

    public InvalidHomeException(String invalidString, Profile profile) {
        this(invalidString + " is not a home.", invalidString, profile);
    }

    public InvalidHomeException(String invalidString) {
        this(invalidString + " is not a home.", invalidString, null);
    }
}
