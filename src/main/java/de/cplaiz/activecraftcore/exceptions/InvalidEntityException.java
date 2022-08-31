package de.cplaiz.activecraftcore.exceptions;

import lombok.Getter;

@Getter
public class InvalidEntityException extends ActiveCraftException {

    private final String invalidString;

    public InvalidEntityException(String message, String invalidString) {
        super(message);
        this.invalidString = invalidString;
    }

    public InvalidEntityException(String invalidString) {
        this(invalidString + " isn't a valid name of an entity.", invalidString);
    }

}
