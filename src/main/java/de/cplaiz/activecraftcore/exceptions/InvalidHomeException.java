package de.cplaiz.activecraftcore.exceptions;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Getter;

@Getter
public class InvalidHomeException extends ActiveCraftException {

    private final String invalidString;
    private final Profilev2 profile;

    public InvalidHomeException(String message, String invalidString, Profilev2 profile) {
        super(message);
        this.invalidString = invalidString;
        this.profile = profile;
    }

    public InvalidHomeException(String invalidString, Profilev2 profile) {
        this(invalidString + " is not a home.", invalidString, profile);
    }

    public InvalidHomeException(String invalidString) {
        this(invalidString + " is not a home.", invalidString, null);
    }

    public Profilev2 getProfile() {
        return profile;
    }

    public String getInvalidString() {
        return invalidString;
    }
}
